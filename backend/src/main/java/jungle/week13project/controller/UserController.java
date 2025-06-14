package jungle.week13project.controller;

import jungle.week13project.config.JwtUtils;
import jungle.week13project.model.entity.User;
import jungle.week13project.model.dto.LoginResponse;
import jungle.week13project.repository.UserRepository;
import jungle.week13project.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;



    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody User user) {
        if (userRepository.existsByUserId(user.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디");
        }
        try {
            userService.validatePassword(user.getPassword());
            userService.saveUser(user);
            logger.info("회원가입 성공 user ID : {} ", user.getUserId());
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 조건이 올바르지 않습니다 : {}" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 저장 오류 : " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userService.findByUserId(loginRequest.getUserId());
        if (user != null && BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateToken(user.getUserName(), user.getUserId(), user.getUserEmail());
            return ResponseEntity.ok(new LoginResponse(token, user.getUserName(), user.getUserEmail(), user.getUserId()));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
