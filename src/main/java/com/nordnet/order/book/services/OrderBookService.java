package nordnet.order.book.services;

import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderBookService {

    @Autowired
    private OrderBookRepository orderBookRepository;

    public OrderBook createOrderBook(OrderBook orderBook) {
        return orderBookRepository.save(orderBook);
    }
}