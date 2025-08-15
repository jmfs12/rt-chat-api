package com.jmfs.api.repositories;

import com.jmfs.api.domain.Message;
import com.jmfs.api.domain.User;
import com.jmfs.api.dto.MessageDTO;
import com.jmfs.api.dto.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Test
    @DisplayName("Should get Message successfully from DB")
    void findByUsersIdSuccess() {
        User sender = createUser(new UserDTO(null, "jm", "joanomiguel@gmail.com"));
        User receiver = createUser(new UserDTO(null, "miguel", "jmfs@gmail.com"));

        assertNotNull(sender);
        assertNotNull(receiver);
        MessageDTO data = new MessageDTO("hello miguel", sender.getId(), receiver.getId(), LocalDateTime.now());
        createMessages(data);

        List<Message> result = messageRepository.findByUsersId(sender.getId(), receiver.getId());
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should return empty list when the message do not exist")
    void findByUsersIdFailCase1() {
        User sender = createUser(new UserDTO(null, "jm", "joanomiguel@gmail.com"));
        User receiver = createUser(new UserDTO(null, "miguel", "jmfs@gmail.com"));

        assertNotNull(sender);
        assertNotNull(receiver);

        List<Message> result = messageRepository.findByUsersId(sender.getId(), receiver.getId());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when one of the users does not exist")
    void findByUsersIdFailCase2() {
        List<Message> result = messageRepository.findByUsersId(56L, 65L);
        assertThat(result).isEmpty();
    }

    private Message createMessages(MessageDTO messageDTO) {
        User sender = userRepository.findById(messageDTO.sender()).orElse(null);
        User receiver = userRepository.findById(messageDTO.receiver()).orElse(null);
        Message message = new Message(messageDTO, sender, receiver);
        this.entityManager.persist(message);
        this.entityManager.flush();
        return message;
    }

    private User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        this.entityManager.persist(user);
        this.entityManager.flush();
        return user;
    }

}