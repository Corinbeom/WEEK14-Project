package jungle.week13project.service;

import jungle.week13project.model.entity.User;
import jungle.week13project.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {

        // 비밀번호를 암호화
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public void validatePassword(String password) {
        if (password.length() < 7) {
            throw new IllegalArgumentException("비밀 번호는 최소 8자 이상 이여야 합니다.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("비밀 번호는 적어도 하나의 대문자를 포함해야 합니다");
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("비밀 번호는 적어도 하나의 특수 문자를 포함 해야 합니다");
        }

    }

    public User findByUserId(String userId) {
        Optional<User> userInfo = userRepository.findByUserId(userId);
        return userInfo.orElse(null);
    }

}
