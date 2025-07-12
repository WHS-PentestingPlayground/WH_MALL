package com.whs.dev2.service;

import com.whs.dev2.dto.LoginRequestDto;
import com.whs.dev2.dto.RegisterRequestDto;
import com.whs.dev2.entity.User;
import com.whs.dev2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setAuth(dto.getAuth());
        user.setPoint(0);
        user.setRanks("normal");
        userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        
        if (user == null) {
            return null;
        }

        if (!password.equals(user.getPassword())) {
            return null;
        }

        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
    
    @Transactional
    public void updatePoint(Long userId, Integer additionalPoint) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 포인트 업데이트
        Integer currentPoint = user.getPoint() != null ? user.getPoint() : 0;
        user.setPoint(currentPoint + additionalPoint);
        
        // 포인트가 100,000 이상이면 VIP로 등급 변경
        if (user.getPoint() >= 100000 && !"vip".equals(user.getRanks())) {
            user.setRanks("vip");
        }
        
        userRepository.save(user);
    }
    
    @Transactional
    public void updatePointByUsername(String username, Integer additionalPoint) {
        User user = findByUsername(username);
        updatePoint(user.getId(), additionalPoint);
    }
    
    public Integer getPointByUsername(String username) {
        User user = findByUsername(username);
        return user.getPoint() != null ? user.getPoint() : 0;
    }
    
    public String getRankByUsername(String username) {
        User user = findByUsername(username);
        return user.getRanks() != null ? user.getRanks() : "normal";
    }
}

