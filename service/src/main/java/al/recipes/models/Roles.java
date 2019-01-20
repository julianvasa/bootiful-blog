package al.recipes.models;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(description = "Roles Data Model")

public class Roles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 50, unique = true, name = "role")
    private String role;
}
