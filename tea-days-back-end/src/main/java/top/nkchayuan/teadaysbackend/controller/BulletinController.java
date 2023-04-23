package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.nkchayuan.teadaysbackend.mapper.BulletinMapper;

@Slf4j
@RequestMapping("/bulletin")
@RestController
public class BulletinController {
    @Autowired
    private BulletinMapper bulletinMapper;

    @GetMapping("/all")
    public ResponseEntity<?> getAllBulletin() {
        return ResponseEntity.status(HttpStatus.OK).body(bulletinMapper.getAllBulletin());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getBulletin(@RequestParam Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(bulletinMapper.getBulletin(id));
    }
}
