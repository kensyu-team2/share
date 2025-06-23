package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** トップメニュー */
    @GetMapping("/")
    public String home() {
        return "main_manu"; // templates/main_manu.html を返す
    }

    /** 会員管理メニュー表示 */
    @GetMapping("/member/menu") // URLを /menu に統一
    public String showMemberMenu() {
        return "member/member_mgmt_menu"; // templates/member/member_mgmt_menu.html
    }

    /** 資料管理メニュー表示 */
    @GetMapping("/book/menu") // BookControllerに移動した方が自然ですが、一旦ここに残します
    public String showBookMenu() { // メソッド名のタイポを修正 (showBookrMenu -> showBookMenu)
        return "book/book_mgmt_menu";
    }

    /** 貸出メニュー表示 */
    @GetMapping("/lending/menu")
    public String showLendingMenu() {
        return "lending/lending_menu"; // templates/lending/lending_menu.html
    }

    /** 返却メニュー表示 */
    @GetMapping("/return/menu")
    public String showReturnMenu() {
        return "return/return_menu"; // templates/return/return_menu.html
    }

    /** 連絡業務メニュー表示 */
    @GetMapping("/work/menu") // URLを /menu に統一
    public String showWorkMenu() { // メソッド名のタイポを修正 (showWorkrMenu -> showWorkMenu)
        return "work/work_menu";
    }
}