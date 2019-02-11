package al.recipes.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "Recipes Data Model")

public class Recipes implements Serializable {
    private static final long serialVersionUID = 2L;
    
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 300, name = "name")
    @ApiModelProperty(notes = "Recipe name")
    private String name;
    
    @Column(nullable = true, length = 1000, name = "intro")
    @ApiModelProperty(notes = "Recipe intro")
    private String intro;
    
    @Column(nullable = true, length = 5000, name = "instruction")
    @ApiModelProperty(notes = "Recipe instruction")
    private String instruction;
    
    @Column(nullable = true, length = 1000, name = "image")
    @ApiModelProperty(notes = "Recipe link to an image")
    private String image;
    
    @Column(nullable = true, length = 1000, name = "link")
    @ApiModelProperty(notes = "Recipe link to a page")
    private String link;
    
    @Column(nullable = true, length = 100, name = "time")
    @ApiModelProperty(notes = "Recipe cooking time")
    private String time;
    
    @Column(nullable = true, length = 100, name = "servings")
    @ApiModelProperty(notes = "Recipe servings")
    private String servings;
    
    @Column(nullable = true, length = 100, name = "calories")
    @ApiModelProperty(notes = "Recipe difficulty")
    private String calories;
    
    @Column(nullable = true, length = 100, name = "favorite")
    @ApiModelProperty(notes = "Recipe favorite Y/N")
    private int favorite;
    
    @Column(nullable = true, length = 100, name = "rating")
    @ApiModelProperty(notes = "Recipe tot views")
    private int rating;
    
    @Column(nullable = true, length = 100, name = "posted")
    @ApiModelProperty(notes = "Recipe posted date YYYYMMDD")
    private int posted;
    
    @Column(nullable = true, length = 100, name = "video")
    @ApiModelProperty(notes = "Recipe link to a youtube vide")
    private String video;
    
    @Column(nullable = true, length = 100, name = "category_id", insertable = false, updatable = false)
    @ApiModelProperty(notes = "Recipe category (foreign key)")
    private int category_id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userid")
    private Users user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Categories category;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private List<Ingredients> ingredients;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private List<Tags> tags;
    
    
}
