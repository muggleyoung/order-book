package nordnet.order.book.controllers;

import jakarta.validation.ConstraintViolationException;
import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.services.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-book")
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @PostMapping
    public ResponseEntity<OrderBook> createOrderBook(@RequestBody OrderBook orderBook) {
        OrderBook createdOrderBook = orderBookService.createOrderBook(orderBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderBook);
    }

    // Handle ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        ex.getConstraintViolations().forEach(violation ->
                message.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append(", "));
        // Remove the last comma and space
        if (message.length() > 2) {
            message.setLength(message.length() - 2);
        }
        return ResponseEntity.badRequest().body(message.toString());
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
    }
}