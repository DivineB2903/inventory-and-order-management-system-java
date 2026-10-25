package com.divine.inventorymanagement.repository;

import com.divine.inventorymanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
