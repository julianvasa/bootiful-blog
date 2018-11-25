package al.recipes.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ingredients implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300, name = "recipe_id")
    private int recipe_id;

    @Column(nullable = true, length = 1000, name = "name")
    private String name;


}
