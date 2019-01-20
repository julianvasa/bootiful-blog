package al.recipes.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Tags Data Model")

public class Tags implements Serializable {
    private static final long serialVersionUID = 2L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false, length = 300, name = "recipe_id")
    @ApiModelProperty(notes = "Recipe ID")
    private int recipe_id;
    
    @Column(nullable = true, length = 1000, name = "value")
    @ApiModelProperty(notes = "Tag value")
    private String value;
    
    @Column(nullable = true, length = 1000, name = "start_pos")
    @ApiModelProperty(notes = "Start position inside intro/instruction")
    private int start_pos;
    
    @Column(nullable = true, length = 1000, name = "end_pos")
    @ApiModelProperty(notes = "End position inside intro/instruction")
    private int end_pos;
    
    @Column(nullable = true, length = 1000, name = "intro_instruction")
    @ApiModelProperty(notes = "Intro or Instruction")
    private String intro_instruction;
    
}
