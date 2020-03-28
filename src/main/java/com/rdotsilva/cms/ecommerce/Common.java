package com.rdotsilva.cms.ecommerce;

import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.PageRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import com.rdotsilva.cms.ecommerce.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class Common {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void sharedData(Model model) {

        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();

        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);
    }
}
