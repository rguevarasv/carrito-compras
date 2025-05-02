package com.shopping.orderservice.mapper;

import com.shopping.orderservice.dto.OrderDto;
import com.shopping.orderservice.entity.Customer;
import com.shopping.orderservice.entity.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-01T21:41:57-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public OrderDto toDto(Order entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDto.OrderDtoBuilder orderDto = OrderDto.builder();

        orderDto.customerId( entityCustomerId( entity ) );
        if ( entity.getStatus() != null ) {
            orderDto.status( entity.getStatus().name() );
        }
        if ( entity.getTotalAmount() != null ) {
            orderDto.totalAmount( entity.getTotalAmount().doubleValue() );
        }
        orderDto.id( entity.getId() );
        orderDto.orderDate( entity.getOrderDate() );
        orderDto.orderDetails( orderDetailMapper.toDtoList( entity.getOrderDetails() ) );
        orderDto.shippingAddress( entity.getShippingAddress() );

        return orderDto.build();
    }

    @Override
    public Order toEntity(OrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.customer( orderDtoToCustomer( dto ) );
        if ( dto.getStatus() != null ) {
            order.status( Enum.valueOf( Order.OrderStatus.class, dto.getStatus() ) );
        }
        order.id( dto.getId() );
        order.orderDate( dto.getOrderDate() );
        if ( dto.getTotalAmount() != null ) {
            order.totalAmount( BigDecimal.valueOf( dto.getTotalAmount() ) );
        }
        order.shippingAddress( dto.getShippingAddress() );

        return order.build();
    }

    @Override
    public List<OrderDto> toDtoList(List<Order> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OrderDto> list = new ArrayList<OrderDto>( entities.size() );
        for ( Order order : entities ) {
            list.add( toDto( order ) );
        }

        return list;
    }

    private Long entityCustomerId(Order order) {
        if ( order == null ) {
            return null;
        }
        Customer customer = order.getCustomer();
        if ( customer == null ) {
            return null;
        }
        Long id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Customer orderDtoToCustomer(OrderDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.id( orderDto.getCustomerId() );

        return customer.build();
    }
}
