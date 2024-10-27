package nordnet.order.book.services;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.model.OrderBookSummary;
import nordnet.order.book.repositories.OrderBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class OrderBookServiceTest {

    @Mock
    private OrderBookRepository orderBookRepository;

    @InjectMocks
    private OrderBookService orderBookService;

    private final String ticker = "SAVE";
    private final String date = "2024-01-01"; // Use a valid date string
    private final String side = "buy";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrderBook_ValidFields() {
        OrderBook orderBook = new OrderBook("SAVE", 10, 100, "buy", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator(); // Get the validator from the factory
            var violations = validator.validate(orderBook);
            assertTrue(violations.isEmpty(), "Should not have any validation errors");
        }
    }

    @Test
    public void testCreateOrderBook_EmptyTicker() {
        OrderBook orderBook = new OrderBook("", 10, 100, "buy", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for empty ticker");
            assertEquals(1, violations.size()); // Expecting error for ticker
        }
    }

    @Test
    public void testCreateOrderBook_NegativeQuantity() {
        OrderBook orderBook = new OrderBook("SAVE", -5, 100, "buy", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for negative quantity");
            assertEquals(1, violations.size()); // Expecting error for quantity
        }
    }

    @Test
    public void testCreateOrderBook_ZeroQuantity() {
        OrderBook orderBook = new OrderBook("SAVE", 0, 100, "buy", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for zero quantity");
            assertEquals(1, violations.size()); // Expecting error for quantity
        }
    }

    @Test
    public void testCreateOrderBook_NegativePrice() {
        OrderBook orderBook = new OrderBook("SAVE", 10, -50, "buy", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for negative price");
            assertEquals(1, violations.size()); // Expecting error for price
        }
    }

    @Test
    public void testCreateOrderBook_ZeroPrice() {
        OrderBook orderBook = new OrderBook("SAVE", 10, 0, "buy", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for zero price");
            assertEquals(1, violations.size()); // Expecting error for price
        }
    }

    @Test
    public void testCreateOrderBook_EmptySide() {
        OrderBook orderBook = new OrderBook("SAVE", 10, 100, "", "USD");

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for empty side");
            assertEquals(1, violations.size()); // Expecting error for side
        }
    }

    @Test
    public void testCreateOrderBook_EmptyCurrency() {
        OrderBook orderBook = new OrderBook("SAVE", 10, 100, "buy", ""); // Invalid currency

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for empty currency");
            assertEquals(1, violations.size()); // Expecting error for currency
        }
    }

    @Test
    public void testCreateOrderBook_AllInvalidFields() {
        OrderBook orderBook = new OrderBook("", -10, -50, "", ""); // Invalid empty fields

        // Validate the orderBook
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(orderBook);
            assertFalse(violations.isEmpty(), "Should have validation errors for all fields");
            assertEquals(5, violations.size()); // Expecting errors for all fields
        }
    }

    @Test
    public void testGetOrderBookSummary_Success() {
        // Arrange
        List<OrderBook> orderBooks = new ArrayList<>();
        orderBooks.add(new OrderBook("SAVE", 2, 200, "buy", "USD"));
        orderBooks.add(new OrderBook("SAVE", 2, 100, "buy", "USD"));

        when(orderBookRepository.findAllByTickerAndSideAndDateBetween(anyString(), any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(orderBooks);

        // Act
        OrderBookSummary summary = orderBookService.getOrderBookSummary(ticker, date, side);

        // Assert
        assertEquals(2, summary.getOrderCount());
        assertEquals(150.0, summary.getAveragePrice());
        assertEquals(200.0, summary.getMaxPrice());
        assertEquals(100.0, summary.getMinPrice());
    }

    @Test
    public void testGetOrderBookSummary_NoOrders() {
        // Arrange
        when(orderBookRepository.findAllByTickerAndSideAndDateBetween(anyString(), any(Date.class), any(Date.class), any(String.class)))
                .thenReturn(new ArrayList<>()); // No orders

        // Act
        OrderBookSummary summary = orderBookService.getOrderBookSummary(ticker, date, side);

        // Assert
        assertEquals(0, summary.getOrderCount());
        assertEquals(0.0, summary.getAveragePrice());
        assertEquals(0.0, summary.getMaxPrice());
        assertEquals(0.0, summary.getMinPrice());
    }

    @Test
    public void testGetOrderBookSummary_SingleOrder() {
        // Arrange
        List<OrderBook> orderBooks = new ArrayList<>();
        orderBooks.add(new OrderBook("SAVE", 5, 150, "buy", "USD"));

        when(orderBookRepository.findAllByTickerAndSideAndDateBetween(anyString(), any(), any(), anyString()))
                .thenReturn(orderBooks);

        // Act
        OrderBookSummary summary = orderBookService.getOrderBookSummary(ticker, date, side);

        // Assert
        assertEquals(1, summary.getOrderCount());
        assertEquals(150.0, summary.getAveragePrice());
        assertEquals(150.0, summary.getMaxPrice());
        assertEquals(150.0, summary.getMinPrice());
    }
}
