package com.jmfs.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jmfs.api.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Long, Message>{
       @Query("SELECT DISTINCT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId)"
              + " OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)")
       public List<Message> findByUsersId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}
