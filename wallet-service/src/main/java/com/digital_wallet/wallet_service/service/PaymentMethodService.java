package com.digital_wallet.wallet_service.service;

import com.digital_wallet.wallet_service.dto.PaymentMethodDto;
import com.digital_wallet.wallet_service.model.PaymentMethod;
import com.digital_wallet.wallet_service.repository.PaymentMethodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepo paymentMethodRepo;

    public void createPaymentMethod(PaymentMethodDto paymentMethodDto){
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setUserId(paymentMethodDto.getUserId());
        paymentMethod.setMethodType(paymentMethodDto.getMethodType());
        paymentMethod.setAccountId(paymentMethodDto.getAccountId());
        paymentMethod.setDetailMasked(paymentMethodDto.getDetailMasked());
        paymentMethod.setIsDefault(paymentMethodDto.getIsDefault());
        paymentMethodRepo.save(paymentMethod);
    }

    public List<PaymentMethodDto> getAllPaymentMethod(Long userId){
        List<PaymentMethod> paymentMethods = paymentMethodRepo.findAllByUserId(userId);
        return paymentMethods.stream().map(paymentMethod -> {
            PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
            paymentMethodDto.setId(paymentMethod.getId());
            paymentMethodDto.setUserId(paymentMethod.getUserId());
            paymentMethodDto.setMethodType(paymentMethod.getMethodType());
            paymentMethodDto.setDetailMasked(paymentMethod.getDetailMasked());
            paymentMethodDto.setIsDefault(paymentMethod.getIsDefault());
            return paymentMethodDto;
        }).collect(Collectors.toList());
    }
}
