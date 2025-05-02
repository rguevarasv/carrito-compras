package com.shopping.orderservice.mapper;

import com.shopping.orderservice.dto.OrderDetailDto;
import com.shopping.orderservice.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(target = "order", ignore = true)
    OrderDetail toEntity(OrderDetailDto dto);

    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "subtotal", source = "subtotal")
    OrderDetailDto toDto(OrderDetail entity);

    List<OrderDetailDto> toDtoList(List<OrderDetail> entities);
    List<OrderDetail> toEntityList(List<OrderDetailDto> dtos);
}