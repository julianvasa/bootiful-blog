package al.recipes.services;

import al.recipes.models.Categories;
import al.recipes.repositories.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    private final CategoryRepository categoryRepository;

    public CategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Categories> findAll() {
        return this.categoryRepository.findAll(new Sort(Sort.Direction.DESC, "count"));
    }

    public Categories find(Long id) {
        return this.categoryRepository.getOne(id);
    }

}
