package com.gusaini.order.service;

import com.gusaini.order.entity.PurchaseOrder;
import com.gusaini.order.repository.PurchaseOrderRepository;
import com.gusaini.dto.OrderRequestDto;
import com.gusaini.events.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class OrderCommandService {

    @Autowired
    private Map<Integer, Integer> productPriceMap;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private OrderStatusPublisher publisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDTO){
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.save(this.dtoToEntity(orderRequestDTO));
        this.publisher.raiseOrderEvent(purchaseOrder, OrderStatus.ORDER_CREATED);
        return purchaseOrder;
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDto dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(productPriceMap.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

}
