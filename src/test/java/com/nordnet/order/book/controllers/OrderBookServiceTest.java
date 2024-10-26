package nordnet.order.book.controllers;

import nordnet.order.book.services.OrderBookService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import nordnet.order.book.entities.OrderBook;
import org.mockito.InjectMocks;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class OrderBookServiceTest {

    @InjectMocks
    private OrderBookService orderBookService;

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
}
