package api.giybat.uz.api.giybat.uz.controller;

import api.giybat.uz.api.giybat.uz.dto.PostDTO;
import api.giybat.uz.api.giybat.uz.util.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {

    @PostMapping("/create")
    public String create(@RequestBody PostDTO postDTO){
        log.info(SpringSecurityUtil.getCurrentProfile().getName() + " " +  SpringSecurityUtil.getCurrentProfile().getUsername());
        System.out.println(SpringSecurityUtil.getCurrentProfile());
        System.out.println(SpringSecurityUtil.getCurrentUserId());
        return "ooooooooooookkkkkkk!!!!!!";
    }
}
