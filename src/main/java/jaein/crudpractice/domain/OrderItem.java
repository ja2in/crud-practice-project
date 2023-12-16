package jaein.crudpractice.domain;

import jaein.crudpractice.domain.item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int count; //도서 갯수

    public static OrderItem createOrderItem(Optional<Item> item, int count) {
        OrderItem orderItem = new OrderItem();
        Item item1 = item.orElseThrow(() -> new IllegalArgumentException("no such item"));

        orderItem.setItem(item.orElse(null));
        orderItem.setCount(count);

        item1.removeStock(count);  //주문이 들어오면 수량이 줄어든다.
        return orderItem;
    }

    public void cancel(){
        getItem().addStock(count);  //주문이 취소되면 수량이 늘어난다.
    }
}
