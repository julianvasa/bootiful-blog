package al.recipes.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Tags implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 300, name = "recipe_id")
    private int recipe_id;

    @Column(nullable = true, length = 1000, name = "value")
    private String value;

    @Column(nullable = true, length = 1000, name = "start_pos")
    private int start_pos;

    @Column(nullable = true, length = 1000, name = "end_pos")
    private int end_pos;

    @Column(nullable = true, length = 1000, name = "intro_instruction")
    private String intro_instruction;

}
