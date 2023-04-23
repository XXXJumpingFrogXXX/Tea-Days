package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.domain.*;
import top.nkchayuan.teadaysbackend.mapper.UserMapper;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    public static HashMap<String, Integer> sessionLookup = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInUser user) {
        User verifyUser = userMapper.mail_selectUser(user.getMailbox());
        HttpStatus status = HttpStatus.OK;
        if (ObjectUtils.isEmpty(user.getMailbox())) {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        if (ObjectUtils.isEmpty(user.getPassword())) {
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        if (verifyUser == null) {
            status = HttpStatus.EXPECTATION_FAILED;
        } else if (!Objects.equals(user.getPassword(), verifyUser.getPassword())) {
            status = HttpStatus.EXPECTATION_FAILED;
        }
        if (status == HttpStatus.OK) {
            String md5str = generateSecureRandomString();
            sessionLookup.put(md5str, verifyUser.getUser_id());
            ResponseCookie springCookie = ResponseCookie.from("session", md5str)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .build();
            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.uid = verifyUser.getUser_id();
            sessionInfo.role = verifyUser.getRole();
            return ResponseEntity.status(status)
                    .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                    .body(sessionInfo);
        } else {
            return ResponseEntity.status(status)
                    .build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUser user) {
        HttpStatus status = HttpStatus.OK;
        if (ObjectUtils.isEmpty(user.getMailbox())) {
            status = HttpStatus.NOT_ACCEPTABLE;
            return ResponseEntity.status(status)
                    .build();
        }
        if (ObjectUtils.isEmpty(user.getNickname())) {
            status = HttpStatus.NOT_ACCEPTABLE;
            return ResponseEntity.status(status)
                    .build();
        }
        if (ObjectUtils.isEmpty(user.getPassword())) {
            status = HttpStatus.NOT_ACCEPTABLE;
            return ResponseEntity.status(status)
                    .build();
        }

        User verifyUser = userMapper.mail_selectUser(user.getMailbox());
        if (verifyUser != null) {
            status = HttpStatus.PRECONDITION_FAILED;
            return ResponseEntity.status(status)
                    .build();
        }

        LocalDateTime start = LocalDateTime.of(2022, 11, 1, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        int sYear=start.getYear();
        int nYear=now.getYear();
        int sDay=start.getDayOfYear();
        int nDay=now.getDayOfYear();
        int sHour=start.getHour();
        int nHour=now.getHour();

        int dayDis=365*(nYear-sYear)+(nDay-sDay);
        int timeDis=24*dayDis+(nHour-sHour);

        int resultCount = userMapper.saveUser(user.getMailbox(), user.getNickname(), user.getPassword(), timeDis);

        if (resultCount == 0) {
            status = HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .build();
        } else {
            return ResponseEntity.status(status)
                    .build();
        }
    }
    @GetMapping("/userinfo")
    public ResponseEntity<?> userinfo(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            UserInfo user=userMapper.id_selectUser(sessionLookup.get(session));
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @PostMapping("/money")
    public ResponseEntity<?> getMoney(@CookieValue(name = "session", defaultValue = "") String session,@RequestParam Integer money){
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            userMapper.updatemoney(money,sessionLookup.get(session));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("/validate_session")
    public ResponseEntity<?> invalidate(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.uid = sessionLookup.get(session);
            sessionInfo.role = userMapper.queryRole(sessionLookup.get(session));
            return ResponseEntity.status(HttpStatus.OK).body(sessionInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/quit")
    public ResponseEntity<?> quit(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            sessionLookup.remove(session);
            ResponseCookie springCookie = ResponseCookie.from("session", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, springCookie.toString()).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private final Random random = new SecureRandom();
    private String generateSecureRandomString() {
        StringBuilder session = new StringBuilder();
        final char[] words = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789".toCharArray();
        for (int i = 0; i < 24; i++) {
            session.append(words[random.nextInt(words.length)]);
        }
        return session.toString();
    }
}