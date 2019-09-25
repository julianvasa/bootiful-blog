package al.recipes.services;

import al.recipes.models.Tags;
import al.recipes.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {

    private final TagRepository tagRepository;

    public TagsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tags> findRandomTags() {
        return this.tagRepository.getRandomTags();
    }

}
