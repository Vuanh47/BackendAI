package org.example.backendai.repository;

import org.example.backendai.entity.Message;
import org.example.backendai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // Tìm messages theo roomId (cho group chat)
    List<Message> findByRoomIdOrderBySentAtAsc(String roomId);

    // Tìm messages giữa 2 users (direct chat)
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.receiver = :user2) OR " +
            "(m.sender = :user2 AND m.receiver = :user1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findMessagesBetweenUsers(@Param("user1") User user1,
                                           @Param("user2") User user2);

    // Tìm messages theo sender
    List<Message> findBySenderOrderBySentAtDesc(User sender);

    // Tìm messages chưa đọc của receiver
    List<Message> findByReceiverAndIsReadFalseOrderBySentAtDesc(User receiver);

    // Đếm messages chưa đọc
    Long countByReceiverAndIsReadFalse(User receiver);

    // Tìm messages trong khoảng thời gian
    List<Message> findBySentAtBetweenOrderBySentAtDesc(LocalDateTime start, LocalDateTime end);

    // Xóa messages theo roomId
    void deleteByRoomId(String roomId);

    // Tìm tất cả rooms mà user tham gia
    @Query("SELECT DISTINCT m.roomId FROM Message m WHERE " +
            "m.sender = :user OR m.receiver = :user " +
            "ORDER BY m.roomId")
    List<String> findRoomsByUser(@Param("user") User user);
}