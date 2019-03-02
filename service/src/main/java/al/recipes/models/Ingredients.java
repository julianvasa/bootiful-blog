package al.recipes.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "Ingredients Data Model")

public class Ingredients implements Serializable {
    private static final long serialVersionUID = 2L;
    
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 300, name = "recipe_id")
    @ApiModelProperty(notes = "Recipe ID")
    private int recipe_id;
    
    @Column(nullable = true, length = 1000, name = "name")
    @ApiModelProperty(notes = "Ingredient desc")
    private String name;
    
    
}
