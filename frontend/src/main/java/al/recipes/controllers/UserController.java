package al.recipes.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping
    public String index(Model model) {    	
    	/*List<Recipe> recipes = new ArrayList<Recipe>();
    	List<Recipe> latest10Recipes = recipeService.findLatest10();
    	for (Recipe recipe : latest10Recipes) {
    		Random rand = new Random(); 
        	int index = rand.nextInt(cssClass.length); 
        	recipe.setCssClassText(cssClassText[index]);
        	recipe.setCssClassBorder(cssClassBorder[index]);
			recipe.setCssClass(cssClass[index]);
			recipe.setInstruction(recipe.getInstruction().substring(0,100)+" ...");
			recipes.add(recipe);
		}	
    	model.addAttribute("recipes", recipes);   */
        return "index";
    }
}

