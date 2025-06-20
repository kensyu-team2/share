package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** トップメニュー */
    @GetMapping("/")
    public String home() {
        return "main_manu";   // templates/main_manu.html を返す
    }

    /** 会員管理メニュー表示 */
    @GetMapping("/member/member")
    public String showMemberMenu() {
        return "member/member_mgmt_menu";  // templates/member/member_mgmt_menu.html
    }

    // 会員一覧
    @GetMapping("/member/member_list")
    public String showMemberList() {
        return "member/member_list"; // このファイルを返す
    }

    /** 資料管理メニュー表示 */
    @GetMapping("/book/book")
    public String showBookrMenu() {
        return "book/book_mgmt_menu";
    }
}
