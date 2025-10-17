package com.jhj.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
   
	@RequestMapping(value = {"/{path:^(?!api$|static|index\\.html$).*$}/**" })
    public String forward() {
        return "forward:/index.html";
    }
   // api 요청 -> 스프링 부트 요청
   // api 요청을 제외한 요청을 React 라우터 요청으로 변경 -> React의 "/"로 시작하는 요청으로 변경
}