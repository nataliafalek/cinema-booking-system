package com.faleknatalia.cinemaBookingSystem.payment;

import javax.persistence.*;

@Entity
public class OrderRequestsAndResponseDB {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long operationId;

    private String extOrderId;
    private long reservationId;
    private String type;

    @Lob
    @Column( length = 100000 )
    private String orderRequestBody;

    public OrderRequestsAndResponseDB(String extOrderId, long reservationId, String type, String orderRequestBody) {
        this.extOrderId = extOrderId;
        this.reservationId = reservationId;
        this.type = type;
        this.orderRequestBody = orderRequestBody;
    }

    public OrderRequestsAndResponseDB() {
    }

    public long getOperationId() {
        return operationId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public long getReservationId() {
        return reservationId;
    }

    public String getOrderRequestBody() {
        return orderRequestBody;
    }

    public String getType() {
        return type;
    }
}
