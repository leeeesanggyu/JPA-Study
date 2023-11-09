package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * TODO OneToMany 페이징 하는 방법, 1 + N 문제 해결 블로깅 해야됨
     */
    @GetMapping("/order/test")
    public List<OrderDto> getOrder() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream().map(
                order -> new OrderDto(
                        order.getId(),
                        new MemberDto(order.getMember().getName()),
                        new ItemDto(order.getOrderItems().stream()
                                .map(orderItem -> orderItem.getItem().getName())
                                .collect(Collectors.toList())),
                        order.getOrderDate(),
                        order.getStatus()
                )
        ).collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    static class OrderDto {
        private Long id;
        private MemberDto member;
        private ItemDto itemDto;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    static class MemberDto {
        private String userName;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    static class ItemDto {
        private List<String> itemName;
    }
}
