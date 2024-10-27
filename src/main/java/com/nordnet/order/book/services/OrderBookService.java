package nordnet.order.book.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.model.OrderBookSummary;
import nordnet.order.book.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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

    public OrderBookSummary getOrderBookSummary(String ticker, String date, String side) {
        LocalDate localDate = LocalDate.parse(date);
        Date parsedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Calculate the next day's date
        LocalDate nextDayLocalDate = localDate.plusDays(1);
        Date nextDayParsedDate = Date.from(nextDayLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<OrderBook> orders = orderBookRepository.findAllByTickerAndSideAndDateBetween(ticker, parsedDate, nextDayParsedDate, side);
        return getOrderBookSummary(ticker, date, side, orders);
    }

    private OrderBookSummary getOrderBookSummary(String ticker, String date, String side, List<OrderBook> orderBooks) {
        double totalQuantity = orderBooks.stream().mapToInt(OrderBook::getQuantity).sum();
        double totalPrice = orderBooks.stream().mapToDouble(order -> order.getPrice() * order.getQuantity()).sum();
        double avgPrice = totalQuantity > 0 ? totalPrice / totalQuantity : 0.0;
        double maxPrice = orderBooks.stream().mapToDouble(OrderBook::getPrice).max().orElse(0.0);
        double minPrice = orderBooks.stream().mapToDouble(OrderBook::getPrice).min().orElse(0.0);
        String currency = orderBooks.isEmpty() ? "" : orderBooks.get(0).getCurrency();
        return new OrderBookSummary(ticker, date, side, orderBooks.size(), avgPrice, maxPrice, minPrice, currency);
    }
}