package nordnet.order.book.services;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderBookService {

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private Validator validator;

    public OrderBook createOrderBook(OrderBook orderBook) {
        Set<ConstraintViolation<OrderBook>> violations = validator.validate(orderBook);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return orderBookRepository.save(orderBook);
    }

    public Optional<OrderBook> getOrderBookById(UUID id) {
        return orderBookRepository.findById(id);
    }
}