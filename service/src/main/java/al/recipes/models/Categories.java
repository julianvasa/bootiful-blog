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

;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "Categories Data Model")

public class Categories implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    @ApiModelProperty(notes = "Name should have at least 2 characters")
    @Column(nullable = false, length = 100, unique = false, name = "name")
    private String name;
    @ApiModelProperty(notes = "Image is a link to an image for each category")
    @Column(nullable = true, length = 100, unique = false, name = "image")
    private String image;
    @ApiModelProperty(notes = "Count is the number of recipes of the current category")
    @Column(nullable = true, length = 11, unique = false, name = "count")
    private int count;
    
    
}
