package com.banksystem.services;

import com.banksystem.domain.user.User;
import com.banksystem.domain.user.UserType;
import com.banksystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
  @Autowired
  private UserRepository repository;

  public void validateTransaction(User sender, BigDecimal amount) throws Exception {
    if (sender.getUserType() == UserType.MERCHANT) {
      throw new Exception("Usuário do tipo lojista não autorizado");
    }

    if (sender.getBalance().compareTo(amount) < 3) {
      throw new Exception("Saldo insuficiente");
    }
  }

  public User findUserById(Long id) throws Exception {
    return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
  }

  public void saveUser(User user) {
    this.repository.save(user);
  }
}
