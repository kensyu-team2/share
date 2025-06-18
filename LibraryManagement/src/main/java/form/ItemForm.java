package form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ItemForm {

    private Integer id;
    @Pattern(regexp = ".{1,10}")
    private String name;
    @NotNull()
    private Integer price;
    private Integer version;

}
