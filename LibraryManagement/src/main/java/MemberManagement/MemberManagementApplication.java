package MemberManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; // この行を追加

@SpringBootApplication
public class MemberManagementApplication {
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ItemManagementApplication {

    public void main(String[] args) {
        SpringApplication.run(MemberManagementApplication.class, args);
    }

}
}
