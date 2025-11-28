package org.example.backendai.repository;

import org.example.backendai.entity.Message;
import org.example.backendai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    @Query("SELECT m FROM Message m WHERE " +
            "m.sender.id = :patientId AND m.receiver.id = :doctorId AND m.isRead = false " +
            "ORDER BY m.sentAt ASC")
    List<Message> findUnreadMessagesByPatientAndDoctorId(
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId);


    Optional<Message> findByReceiverId(Integer receiverId);

    @Query("SELECT m FROM Message m WHERE " +
            "m.receiver.id = :receiverId AND m.isRead = false " +
            "ORDER BY m.sentAt ASC")
    Optional<Message> findFirstUnreadMessageByReceiverId(@Param("receiverId") Long receiverId);

    // Hoặc nếu bạn muốn lấy tất cả tin nhắn chưa đọc từ bệnh nhân
    @Query("SELECT m FROM Message m WHERE " +
            "m.receiver.id = :receiverId AND m.isRead = false " +
            "ORDER BY m.sentAt ASC")
    List<Message> findUnreadMessagesByReceiverId(@Param("receiverId") Long receiverId);


    // Count unread messages where doctor is receiver and patient is sender
    Long countByReceiverAndSenderAndIsReadFalse(User receiver, User sender);

    // Other existing methods...
    List<Message> findByReceiverAndSenderAndIsReadFalseOrderBySentAtDesc(User receiver, User sender);

}