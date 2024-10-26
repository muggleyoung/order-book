package nordnet.order.book.repositories;

import nordnet.order.book.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderBookRepository extends JpaRepository<OrderBook, UUID> {
}