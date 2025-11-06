package com.example.logi.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.logi.domain.product.Product;
import com.example.logi.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    /** ✅ 목록 */
    @GetMapping
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       Model model,
                       @ModelAttribute("message") String message) {

        int pageSize = 5;                 // 한 페이지에 5개
        int pageIndex = Math.max(page - 1, 0);   // 0-based

        Page<Product> productPage = service.getPage(pageIndex, pageSize);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);

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
/** 상품 수정 폼 (상세 + 수정 화면) */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Product product = service.findById(id);
        if (product == null) {
            // 없으면 목록으로 돌려보내도 되고, 에러 페이지로 보내도 됨
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        return "admin/products/edit"; // → edit.html
    }

    /** 수정 처리 */
    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult bindingResult,
                         RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            return "admin/products/edit";
        }

        try {
            service.update(id, product);   // ← ProductService에 이미 있는 update 사용
            ra.addFlashAttribute("message", "商品が修正されました。");
            return "redirect:/admin/products";
        } catch (DataIntegrityViolationException dup) {
            bindingResult.rejectValue("sku", "duplicate", "既に使用されているSKUです");
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