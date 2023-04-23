package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.domain.UpdatedUserInfo;
import top.nkchayuan.teadaysbackend.mapper.UserMapper;

@Slf4j
@RequestMapping("/admin_user")
@RestController
public class AdminUserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("list_all")
    public ResponseEntity<?> listAll(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2)
                return ResponseEntity.status(HttpStatus.OK).body(userMapper.selectAll());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("remove_user")
    public ResponseEntity<?> removeUser(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam int userId) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2) {
                this.userMapper.removeUser(userId);
                return ResponseEntity.status(HttpStatus.OK).body(userMapper.selectAll());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("update")
    public ResponseEntity<?> updateInfo(@CookieValue(name = "session", defaultValue = "") String session, @RequestBody UpdatedUserInfo userInfo) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userInfo.getRole() < 0 || userInfo.getRole() > 2 || userInfo.getUser_id() == null || userInfo.getMoney() == null || userInfo.getNickname() == null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
            if (userMapper.queryRole(uid) == 2) {
                userMapper.updateUserInfo(userInfo.getUser_id(), userInfo.getNickname(), userInfo.getMoney(), userInfo.getRole());
                return ResponseEntity.status(HttpStatus.OK).body(userMapper.id_selectUser(userInfo.getUser_id()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
