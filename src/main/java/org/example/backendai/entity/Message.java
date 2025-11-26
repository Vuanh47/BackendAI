package org.example.backendai.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.MessageType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    User receiver;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @Column(length = 500)
    String attachmentURL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    MessageType type;

    @Column(length = 100)
    String roomId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime sentAt;

    @Builder.Default
    @Column(nullable = false)
    Boolean isRead = false;


}