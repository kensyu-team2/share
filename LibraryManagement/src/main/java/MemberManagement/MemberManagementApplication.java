package MemberManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; // この行を追加

<<<<<<< HEAD:LibraryManagement/src/main/java/MemberManagement/MemberManagementApplication.java
@SpringBootApplication
public class MemberManagementApplication {
=======
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ItemManagementApplication {
>>>>>>> 82d74469911ead3b92871138fbf17bbfe77b0c8b:LibraryManagement/src/main/java/jp/co/systempack/itemManagement/ItemManagementApplication.java

    public static void main(String[] args) {
        SpringApplication.run(MemberManagementApplication.class, args);
    }

}
