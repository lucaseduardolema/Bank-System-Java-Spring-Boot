package com.banksystem.services;

import com.banksystem.domain.transaction.Transaction;
import com.banksystem.domain.user.User;
import com.banksystem.dtos.TransactionDTO;
import com.banksystem.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionsService {
  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository repository;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private NotificationService notificationService;

  public Transaction createTransaction(TransactionDTO transaction) throws Exception {
    User sender = this.userService.findUserById(transaction.senderId());
    User receiver = this.userService.findUserById(transaction.receiverId());

    userService.validateTransaction(sender, transaction.value());

    boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
    if (!isAuthorized) throw new Exception("Transação não autorizada");

    Transaction newtransaction = new Transaction();
    newtransaction.setAmount(transaction.value());
    newtransaction.setSender(sender);
    newtransaction.setReceiver(receiver);
    newtransaction.setTimestamp(LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transaction.value()));
    receiver.setBalance(receiver.getBalance().add(transaction.value()));

    this.repository.save(newtransaction);
    this.userService.saveUser(sender);
    this.userService.saveUser(receiver);

    //this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
    //this.notificationService.sendNotification(receiver, "Transação recebida");

    return newtransaction;
  }

  public boolean authorizeTransaction(User sender, BigDecimal value) {
    ResponseEntity<Map> authorizarionResponse = restTemplate.getForEntity(
            "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6",
            Map.class
    );

    if (authorizarionResponse.getStatusCode() == HttpStatus.OK) {
      String message = (String) authorizarionResponse.getBody().get("message");
      return "autorizado".equalsIgnoreCase(message);
    } else {
      return false;
    }
  }
}
