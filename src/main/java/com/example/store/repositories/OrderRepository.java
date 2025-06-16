package com.example.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Order;
import com.example.store.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomer(User user);

}
