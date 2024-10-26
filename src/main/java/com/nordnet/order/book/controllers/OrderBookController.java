package nordnet.order.book.controllers;

import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.services.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<OrderBook> getOrderBookById(@PathVariable UUID id) {
        Optional<OrderBook> orderBook = orderBookService.getOrderBookById(id);
        return orderBook.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}