package com.example.logi.controller;

import com.example.logi.domain.product.Product;
import com.example.logi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    /** ✅ 목록 */
    @GetMapping
    public String list(Model model, @ModelAttribute("message") String message) {
        model.addAttribute("products", service.findAll());
        return "admin/products/productList";
    }

    /** 신규 등록 폼 */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/new";
    }

    /** ✅ 신규 등록 처리 */
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("product") Product product,
                         BindingResult bindingResult,
                         RedirectAttributes ra) {
        // 1) Bean Validation 에러
        if (bindingResult.hasErrors()) {
            return "admin/products/new";
        }

        // 2) ✅ 중복 SKU 사전 체크(권장) — 동시성에 대비해 아래 try-catch도 유지
        if (service.existsSku(product.getSku())) {
            bindingResult.rejectValue("sku", "duplicate", "이미 사용 중인 SKU입니다");
            return "admin/products/new";
        }

        try {
            service.create(product);
            ra.addFlashAttribute("message", "상품이 등록되었습니다.");
            return "redirect:/admin/products";
        } catch (DataIntegrityViolationException dup) {
            // DB Unique 제약 위반(동시성 등)
            bindingResult.rejectValue("sku", "duplicate", "이미 사용 중인 SKU입니다");
            return "admin/products/new";
        }
    }

    /** 수정 폼 */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.findById(id));
        return "admin/products/edit";
    }

    /** 수정 처리 */
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult bindingResult,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "admin/products/edit";
        }
        try {
            service.update(id, product);
            ra.addFlashAttribute("message", "상품이 수정되었습니다.");
            return "redirect:/admin/products";
        } catch (DataIntegrityViolationException dup) {
            bindingResult.rejectValue("sku", "duplicate", "이미 사용 중인 SKU입니다");
            return "admin/products/edit";
        }
    }

    /** 삭제 */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "상품이 삭제되었습니다.");
        return "redirect:/admin/products";
    }
}