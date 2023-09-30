package com.bwgjoseph.springsecurityrolehierarchybug;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MeController {
    @GetMapping("/me")
    public MyUserDetails me(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userDetails;
    }
}
