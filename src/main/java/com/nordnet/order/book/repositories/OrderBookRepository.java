package nordnet.order.book.repositories;

import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.model.Side;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderBookRepository extends JpaRepository<OrderBook, UUID> {
    @Query("SELECT o FROM OrderBook o WHERE o.ticker = :ticker AND o.side = :side " +
            "AND o.createdAt >= :startDate AND o.createdAt < :endDate")
    List<OrderBook> findAllByTickerAndSideAndDateBetween(
            @Param("ticker") String ticker,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("side") Side side);}