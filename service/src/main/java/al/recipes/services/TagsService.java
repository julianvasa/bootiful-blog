package al.recipes.services;

import al.recipes.models.Tags;
import al.recipes.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tags> findRandomTags() {
        return this.tagRepository.getRandomTags();
    }

}
