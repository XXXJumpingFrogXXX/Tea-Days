package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.domain.BulletinInfo;
import top.nkchayuan.teadaysbackend.domain.UpdatedUserInfo;
import top.nkchayuan.teadaysbackend.mapper.BulletinMapper;
import top.nkchayuan.teadaysbackend.mapper.UserMapper;

@Slf4j
@RequestMapping("/admin_bulletin")
@RestController
public class AdminBulletinController {
    @Autowired
    private BulletinMapper bulletinMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("list_all")
    public ResponseEntity<?> listAll(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2)
                return ResponseEntity.status(HttpStatus.OK).body(bulletinMapper.getAllBulletin());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("remove")
    public ResponseEntity<?> remove(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam int bulletinId) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2) {
                this.bulletinMapper.remove(bulletinId);
                return ResponseEntity.status(HttpStatus.OK).body(bulletinMapper.getAllBulletin());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("update")
    public ResponseEntity<?> updateInfo(@CookieValue(name = "session", defaultValue = "") String session, @RequestBody BulletinInfo bulletinInfo) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (bulletinInfo.title == null || bulletinInfo.content == null || bulletinInfo.bulletin_id == null || bulletinInfo.img_url == null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
            if (userMapper.queryRole(uid) == 2) {
                bulletinMapper.update(bulletinInfo.bulletin_id, bulletinInfo.title, bulletinInfo.content, bulletinInfo.img_url);
                return ResponseEntity.status(HttpStatus.OK).body(bulletinMapper.getBulletin(bulletinInfo.bulletin_id));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("add")
    public ResponseEntity<?> addNew(@CookieValue(name = "session", defaultValue = "") String session, @RequestBody BulletinInfo bulletinInfo) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (bulletinInfo.title == null || bulletinInfo.content == null || bulletinInfo.img_url == null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
            if (userMapper.queryRole(uid) == 2) {
                bulletinMapper.insert(bulletinInfo.title, bulletinInfo.content, bulletinInfo.img_url);
                return ResponseEntity.status(HttpStatus.OK).body(bulletinMapper.getAllBulletin());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
