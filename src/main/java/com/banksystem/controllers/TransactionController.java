package com.banksystem.controllers;

import com.banksystem.domain.transaction.Transaction;
import com.banksystem.dtos.TransactionDTO;
import com.banksystem.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  @Autowired
  private TransactionsService transactionsService;

  @PostMapping
  private ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
    Transaction newTransaction = this.transactionsService.createTransaction(transaction);
    return new ResponseEntity<>(newTransaction, HttpStatus.OK);
  }
}
