package com.shopping.paymentservice.mapper;

import com.shopping.paymentservice.dto.PaymentDto;
import com.shopping.paymentservice.entity.Payment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-01T21:42:04-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDto toDto(Payment entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentDto.PaymentDtoBuilder paymentDto = PaymentDto.builder();

        if ( entity.getStatus() != null ) {
            paymentDto.status( entity.getStatus().name() );
        }
        if ( entity.getAmount() != null ) {
            paymentDto.amount( entity.getAmount().doubleValue() );
        }
        paymentDto.id( entity.getId() );
        paymentDto.orderId( entity.getOrderId() );
        paymentDto.paymentDate( entity.getPaymentDate() );
        paymentDto.paymentMethod( entity.getPaymentMethod() );
        paymentDto.transactionId( entity.getTransactionId() );

        return paymentDto.build();
    }

    @Override
    public Payment toEntity(PaymentDto dto) {
        if ( dto == null ) {
            return null;
        }

        Payment.PaymentBuilder payment = Payment.builder();

        if ( dto.getStatus() != null ) {
            payment.status( Enum.valueOf( Payment.PaymentStatus.class, dto.getStatus() ) );
        }
        if ( dto.getAmount() != null ) {
            payment.amount( BigDecimal.valueOf( dto.getAmount() ) );
        }
        payment.id( dto.getId() );
        payment.orderId( dto.getOrderId() );
        payment.paymentDate( dto.getPaymentDate() );
        payment.paymentMethod( dto.getPaymentMethod() );
        payment.transactionId( dto.getTransactionId() );

        return payment.build();
    }

    @Override
    public List<PaymentDto> toDtoList(List<Payment> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaymentDto> list = new ArrayList<PaymentDto>( entities.size() );
        for ( Payment payment : entities ) {
            list.add( toDto( payment ) );
        }

        return list;
    }
}
