package nordnet.order.book.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter @NoArgsConstructor
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty(message = "Ticker is required")
    @Column(name = "ticker", nullable = false)
    private String ticker;

    @NotNull(message = "Quantity is required")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull(message = "Price is required")
    @Column(name = "price", nullable = false)
    private int price;

    @NotEmpty(message = "Side is required")
    @Column(name = "side", nullable = false)
    private String side;

    @NotEmpty(message = "Currency is required")
    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "createdAt", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}