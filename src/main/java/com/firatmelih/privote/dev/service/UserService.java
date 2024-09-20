package com.firatmelih.privote.dev.service;

import com.firatmelih.privote.dev.dto.PollCreatorDto;
import com.firatmelih.privote.dev.model.User;
import com.firatmelih.privote.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Integer getUserId(String username) {
        User user = userRepository.findByUsername(username);
        return user.getId();
    }

    public User getOneUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public PollCreatorDto userToDto(User user) {
        return PollCreatorDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
