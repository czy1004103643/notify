package xyz.melodyl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.melodyl.annotation.JsonController;
import xyz.melodyl.mapper.table.TableMapper;
import xyz.melodyl.pojo.ResponseBody;

@RestController
@JsonController("/test")
public class TestController {

    @GetMapping
    public ResponseEntity getTest() {
        return new ResponseEntity(new ResponseBody("test"), HttpStatus.OK);
    }
}
