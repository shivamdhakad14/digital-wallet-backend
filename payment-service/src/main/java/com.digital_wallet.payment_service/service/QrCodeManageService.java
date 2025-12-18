package com.digital_wallet.payment_service.service;

import com.digital_wallet.payment_service.client.UserClient;
import com.digital_wallet.payment_service.client.WalletClient;
import com.digital_wallet.payment_service.dto.QrCodeDto;
import com.digital_wallet.payment_service.dto.WalletDto;
import com.digital_wallet.payment_service.enums.QrStatus;
import com.digital_wallet.payment_service.enums.QrType;
import com.digital_wallet.payment_service.model.QrPayment;
import com.digital_wallet.payment_service.repository.QrPaymentRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.netflix.discovery.converters.Auto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class QrCodeManageService {

    @Autowired
    private QrPaymentRepository qrPaymentRepository;
    @Autowired
    private WalletClient walletClient;
    @Autowired
    private UserClient userClient;

    @Transactional
    public ResponseEntity<byte[]> qrCodeGenerate(QrCodeDto qrCodeDto) {
        try {
            List<QrPayment> existingQrs = qrPaymentRepository.findByWalletId(qrCodeDto.getWalletId());

            if (!existingQrs.isEmpty()) {

                QrPayment existingQr = existingQrs.getLast();

                byte[] qrImage = Base64.getDecoder().decode(existingQr.getEncodedData());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=wallet_qr.png")
                        .contentType(MediaType.IMAGE_PNG)
                        .body(qrImage);
            }

            WalletDto wallet = walletClient.getWalletByUserId(qrCodeDto.getUserId());

            String qrContent = String.format(
                    "upi://pay?pa=%s&pn=%s&mode=04&cu=INR&walletId=%s&userId=%s",
                    wallet.getUpiId(),
                    wallet.getWalletName(),
                    wallet.getId(),
                    qrCodeDto.getUserId()
            );

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            String encodedData = Base64.getEncoder().encodeToString(pngData);

            QrPayment qrPayment = new QrPayment();
            qrPayment.setEncodedData(encodedData);
            qrPayment.setWalletId(wallet.getId());
            qrPayment.setUserId(qrCodeDto.getUserId());
            qrPayment.setCurrency("INR");
            qrPayment.setQrStatus(QrStatus.ACTIVE);
            qrPayment.setQrType(QrType.STATIC);
            qrPayment.setUpiId(wallet.getUpiId());
            qrPaymentRepository.save(qrPayment);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=wallet_qr.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(pngData);

        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Error generating QR code: " + e.getMessage(), e);
        }
    }
}
