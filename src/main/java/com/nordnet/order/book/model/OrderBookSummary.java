package nordnet.order.book.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookSummary {
    private String ticker;
    private String date;
    private String side;
    private int orderCount;
    private double averagePrice;
    private double maxPrice;
    private double minPrice;
    private String currency;
}
