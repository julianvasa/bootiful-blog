package al.recipes.models;

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
public class Recipes implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300, name = "name")
    private String name;

    @Column(nullable = true, length = 1000, name = "intro")
    private String intro;

    @Column(nullable = true, length = 5000, name = "instruction")
    private String instruction;

    @Column(nullable = true, length = 1000, name = "image")
    private String image;

    @Column(nullable = true, length = 1000, name = "link")
    private String link;

    @Column(nullable = true, length = 100, name = "time")
    private String time;

    @Column(nullable = true, length = 100, name = "servings")
    private String servings;

    @Column(nullable = true, length = 100, name = "calories")
    private String calories;

    @Column(nullable = true, length = 100, name = "favorite")
    private int favorite;

    @Column(nullable = true, length = 100, name = "rating")
    private int rating;

    @Column(nullable = true, length = 100, name = "posted")
    private int posted;

    @Column(nullable = true, length = 100, name = "video")
    private String video;

    @Column(nullable = true, length = 100, name = "category_id", insertable = false, updatable = false)
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
