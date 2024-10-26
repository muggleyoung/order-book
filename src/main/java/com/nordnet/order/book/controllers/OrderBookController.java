package nordnet.order.book.controllers;

import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.services.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-book")
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @PostMapping
    public OrderBook createOrderBook(@RequestBody OrderBook orderBook) {
        return orderBookService.createOrderBook(orderBook);
    }
}