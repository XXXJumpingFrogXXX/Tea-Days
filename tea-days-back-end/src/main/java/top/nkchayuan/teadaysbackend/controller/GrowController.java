package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.nkchayuan.teadaysbackend.mapper.GrowMapper;

import java.time.LocalDateTime;

import static top.nkchayuan.teadaysbackend.controller.UserController.sessionLookup;

@Slf4j
@RequestMapping("/grow")
@RestController
public class GrowController {
    @Autowired
    private GrowMapper growMapper;

    @PostMapping("/date")
    public ResponseEntity<?> stage(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            LocalDateTime start = LocalDateTime.of(2022, 11, 1, 0, 0);
            LocalDateTime now = LocalDateTime.now();
            int sYear = start.getYear();
            int nYear = now.getYear();
            int sDay = start.getDayOfYear();
            int nDay = now.getDayOfYear();
            int sHour = start.getHour();
            int nHour = now.getHour();

            int dayDis = 365 * (nYear - sYear) + (nDay - sDay);
            int timeDis = 24 * dayDis + (nHour - sHour);
            int currentTimeLen = timeDis - growMapper.register_time(sessionLookup.get(session));
            return ResponseEntity.status(HttpStatus.OK).body(currentTimeLen / 6 % 24);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
