package jp.co.systempack.itemManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; // この行を追加

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ItemManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemManagementApplication.class, args);
    }

}
