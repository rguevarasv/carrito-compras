package com.shopping.orderservice.mapper;

import com.shopping.orderservice.dto.OrderDetailDto;
import com.shopping.orderservice.entity.OrderDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-01T21:41:57-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class OrderDetailMapperImpl implements OrderDetailMapper {

    @Override
    public OrderDetail toEntity(OrderDetailDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderDetail.OrderDetailBuilder orderDetail = OrderDetail.builder();

        orderDetail.id( dto.getId() );
        orderDetail.productId( dto.getProductId() );
        orderDetail.productName( dto.getProductName() );
        orderDetail.quantity( dto.getQuantity() );
        if ( dto.getUnitPrice() != null ) {
            orderDetail.unitPrice( BigDecimal.valueOf( dto.getUnitPrice() ) );
        }
        if ( dto.getSubtotal() != null ) {
            orderDetail.subtotal( BigDecimal.valueOf( dto.getSubtotal() ) );
        }

        return orderDetail.build();
    }

    @Override
    public OrderDetailDto toDto(OrderDetail entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDetailDto.OrderDetailDtoBuilder orderDetailDto = OrderDetailDto.builder();

        if ( entity.getUnitPrice() != null ) {
            orderDetailDto.unitPrice( entity.getUnitPrice().doubleValue() );
        }
        if ( entity.getSubtotal() != null ) {
            orderDetailDto.subtotal( entity.getSubtotal().doubleValue() );
        }
        orderDetailDto.id( entity.getId() );
        orderDetailDto.productId( entity.getProductId() );
        orderDetailDto.productName( entity.getProductName() );
        orderDetailDto.quantity( entity.getQuantity() );

        return orderDetailDto.build();
    }

    @Override
    public List<OrderDetailDto> toDtoList(List<OrderDetail> entities) {
        if ( entities == null ) {
            return null;
        }

        List<OrderDetailDto> list = new ArrayList<OrderDetailDto>( entities.size() );
        for ( OrderDetail orderDetail : entities ) {
            list.add( toDto( orderDetail ) );
        }

        return list;
    }

    @Override
    public List<OrderDetail> toEntityList(List<OrderDetailDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<OrderDetail> list = new ArrayList<OrderDetail>( dtos.size() );
        for ( OrderDetailDto orderDetailDto : dtos ) {
            list.add( toEntity( orderDetailDto ) );
        }

        return list;
    }
}
