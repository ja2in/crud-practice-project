package jaein.crudpractice.api;

import jaein.crudpractice.domain.Order;
import jaein.crudpractice.domain.OrderItem;
import jaein.crudpractice.domain.OrderStatus;
import jaein.crudpractice.repository.OrderRepository;
import jaein.crudpractice.repository.order.query.OrderQueryDto;
import jaein.crudpractice.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findWithItem();

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3.1/orders") //페이징 기능 추가
    public List<OrderDto> ordersV3_page(Pageable pageable) {

        List<Order> orders = orderRepository.findAllWithStudentLoan(pageable);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/orders") //페이징 기능 추가
    public List<OrderQueryDto> orderV4(Pageable pageable) {
        return orderQueryRepository.findOrderQueryDtos(pageable);
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime loanDate;
        private LocalDateTime returnDate;
        private OrderStatus orderStatus;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getStudent().getName();
            loanDate = order.getLoanDate();
            returnDate = order.getReturnDate();
//            orderStatus = order.getStatus();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            count = orderItem.getCount();
        }
    }
}
