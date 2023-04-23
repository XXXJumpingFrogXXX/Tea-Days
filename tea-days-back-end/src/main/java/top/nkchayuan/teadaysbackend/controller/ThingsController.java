package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.mapper.ThingsMapper;

import java.util.ArrayList;

import static top.nkchayuan.teadaysbackend.controller.UserController.sessionLookup;

@Slf4j
@RequestMapping("/things")
@RestController
public class ThingsController {
    @Autowired
    private ThingsMapper thingsMapper;

    @GetMapping("/get")
    public ResponseEntity<?> get(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer type) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            ArrayList<String> a = thingsMapper.getInfo(type);
            return ResponseEntity.status(HttpStatus.OK).body(a);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/get_data")
    public ResponseEntity<?> getFullData(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam Integer type) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.getFullData(type));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("/gettea")
    public ResponseEntity<?> getTea(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            ArrayList<String> a = thingsMapper.getTeaInfo();
            return ResponseEntity.status(HttpStatus.OK).body(a);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/getteaname")
    public ResponseEntity<?> getTeaName(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (sessionLookup.containsKey(session)) {
            ArrayList<String> a = thingsMapper.getTeaName();
            return ResponseEntity.status(HttpStatus.OK).body(a);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
