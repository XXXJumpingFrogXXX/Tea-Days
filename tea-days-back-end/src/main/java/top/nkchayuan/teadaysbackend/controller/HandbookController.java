package top.nkchayuan.teadaysbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.nkchayuan.teadaysbackend.domain.UpdatedThingInfo;
import top.nkchayuan.teadaysbackend.mapper.ThingsMapper;
import top.nkchayuan.teadaysbackend.mapper.UserMapper;

@Slf4j
@RequestMapping("/handbook")
@RestController
public class HandbookController {
    @Autowired
    private ThingsMapper thingsMapper;
    @Autowired
    private UserMapper userMapper;


    @GetMapping("list_tea")
    public ResponseEntity<?> listTea(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2)
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.selectTea());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("list_tool")
    public ResponseEntity<?> listTool(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2)
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.selectTool());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("list_item")
    public ResponseEntity<?> listItem(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2)
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.selectItem());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("list_term")
    public ResponseEntity<?> listTerm(@CookieValue(name = "session", defaultValue = "") String session) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2)
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.selectTerm());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("remove_tool")
    public ResponseEntity<?> removeTool(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam String name) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2) {
                this.thingsMapper.removeThing(name);
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.selectTool());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("remove_item")
    public ResponseEntity<?> removeItem(@CookieValue(name = "session", defaultValue = "") String session, @RequestParam String name) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (userMapper.queryRole(uid) == 2) {
                this.thingsMapper.removeThing(name);
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.selectItem());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("update")
    public ResponseEntity<?> updateThing(@CookieValue(name = "session", defaultValue = "") String session, @RequestBody UpdatedThingInfo thingInfo) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (thingInfo.getType() < 0 || thingInfo.getName() == null || thingInfo.getType() == null || thingInfo.getInfo() == null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
            if (userMapper.queryRole(uid) == 2) {
                thingsMapper.updateThingInfo(thingInfo.getName(), thingInfo.getType(), thingInfo.getInfo());
                return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.name_selectThing(thingInfo.getName()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("insert")
    public ResponseEntity<?> insertThing(@CookieValue(name = "session", defaultValue = "") String session, @RequestBody UpdatedThingInfo thingInfo) {
        if (session == null || session.length() == 0)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if (UserController.sessionLookup.containsKey(session)) {
            int uid = UserController.sessionLookup.get(session);
            if (thingInfo.getType() < 0 || thingInfo.getName() == null || thingInfo.getType() == null || thingInfo.getInfo() == null) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
            try{
                if (userMapper.queryRole(uid) == 2) {
                    thingsMapper.insertThing(thingInfo.getName(), thingInfo.getType(), thingInfo.getInfo());
                    return ResponseEntity.status(HttpStatus.OK).body(thingsMapper.name_selectThing(thingInfo.getName()));
                }
            }catch(Exception exp){
                return ResponseEntity.status(420).build();
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
