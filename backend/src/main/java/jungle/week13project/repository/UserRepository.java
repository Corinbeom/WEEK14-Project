package jungle.week13project.repository;

import jungle.week13project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId); // 사용자 ID로 사용자 정보 조회
    boolean existsByUserId(String userId);  //  Id 중복 검사
    boolean existsByUserName(String userName);  //  닉네임 중복 검사
}
