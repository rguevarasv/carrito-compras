package com.shopping.paymentservice.mapper;

import com.shopping.paymentservice.dto.PaymentDto;
import com.shopping.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "amount", source = "amount")
    PaymentDto toDto(Payment entity);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "amount", source = "amount")
    Payment toEntity(PaymentDto dto);

    List<PaymentDto> toDtoList(List<Payment> entities);
}