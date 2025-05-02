package com.shopping.orderservice.mapper;

import com.shopping.orderservice.dto.OrderDto;
import com.shopping.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "totalAmount", source = "totalAmount")
    OrderDto toDto(Order entity);

    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "orderDetails", ignore = true)
    Order toEntity(OrderDto dto);

    List<OrderDto> toDtoList(List<Order> entities);
}