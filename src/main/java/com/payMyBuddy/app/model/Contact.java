package com.payMyBuddy.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Optional;
@Entity
@Getter
@Setter
@Table(name = "contact")
@IdClass(Contact.ContactId.class)
@RequiredArgsConstructor
public class Contact {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private User contact;

    public Contact(User user, User contact) {
        this.user = user;
        this.contact = contact;
    }

    // Required for composite primary key
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactId implements Serializable {
        private Long user;
        private Long contact;
    }
}