package com.shopme.admin.brand;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {

    private final BrandService brandService;

    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/brands/check_unique")
    public String checkUnique(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "name", required = false) String name) {
        return brandService.checkUnique(id, name);
    }

}
