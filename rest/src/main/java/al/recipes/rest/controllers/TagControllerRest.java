package al.recipes.rest.controllers;

import al.recipes.models.Tags;
import al.recipes.services.TagsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagControllerRest {

    private final TagsService tagsService;

    public TagControllerRest(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping("/tags")
    @ApiOperation(value = "Get random tags", notes = "Get random tags")
    public List<Tags> getRandomTags() {
        return tagsService.findRandomTags();
    }
}
