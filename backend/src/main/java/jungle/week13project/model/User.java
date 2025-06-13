package jungle.week13project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName")
    private String userName;  // 닉네임

    @Column(name = "userId")
    private String userId;

    @Column(name = "password")
    private String password;  // 비밀번호

    @Column(name = "userEmail")
    private String userEmail; // 이메일

    @Column(name = "userBirth")
    private Date userBirth;   // 생일

    @Column(name = "createdTime")
    private Date createdTime; // 가입일
}
