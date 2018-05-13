package com.faleknatalia.cinemaBookingSystem.payment;

import javax.persistence.*;

@Entity
public class NotificationResponseDB {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long notificationResponseDBId;

    private String extOrderId;

    @Lob
    @Column(length = 100000)
    private String notificationResponseBody;

    public NotificationResponseDB(String extOrderId, String notificationResponseBody) {
        this.extOrderId = extOrderId;
        this.notificationResponseBody = notificationResponseBody;
    }

    public NotificationResponseDB() {
    }

    public long getNotificationResponseDBId() {
        return notificationResponseDBId;
    }

    public String getExtOrderId() {
        return extOrderId;
    }

    public String getNotificationResponseBody() {
        return notificationResponseBody;
    }
}
