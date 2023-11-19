package com.poly.controller.admin;

import com.poly.dto.EvaluateRes;
import com.poly.entity.Evaluate;
import com.poly.service.CustomerService;
import com.poly.service.EvaluateService;
import com.poly.service.ProductDetailService;
import com.poly.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class EvaluateController {
    @Autowired
    EvaluateService evaluateService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @GetMapping("/evaluate/{id}")
    public String index(@ModelAttribute(name = "evaluate") EvaluateRes evaluateDto, @PathVariable Integer id, Model model, HttpSession httpSession) {
        evaluateDto.setProduct(id);
        Page<Evaluate> evaluates = evaluateService.getAll(evaluateDto);
        model.addAttribute("list", evaluates);
        httpSession.setAttribute("pageView", "/admin/page/evaluate/evaluate.html");
        httpSession.setAttribute("active", "/evaluate/list");
        model.addAttribute("product", productService.findById(id));
        return "admin/layout";
    }

    @PostMapping("/evaluate/add")
    public String add(@Valid @ModelAttribute("evaluate") EvaluateRes evaluate, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/layout";
        }
        this.evaluateService.add(evaluate);
        return "redirect:/admin/evaluate/list";
    }

    @GetMapping("/evaluate/delete/{id}")
    public String delete(@PathVariable Integer id) {
        evaluateService.delete(id);
        return "redirect:/admin/evaluate/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Evaluate evaluate = evaluateService.findById(id);
        model.addAttribute("evaluate", evaluate);
        return "redirect:/admin/evaluate/list";
    }

    @GetMapping("/evaluate/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Evaluate evaluate = evaluateService.edit(id);
        model.addAttribute("evaluate", evaluate);
        return "redirect:/admin/evaluate/list";
    }

    @PostMapping("/evaluate/update/{id}")
    public String updateAccount(@PathVariable Integer id, @ModelAttribute("evaluate") EvaluateRes evaluate, Model model) {
        evaluateService.add(evaluate);
        System.out.println("sua thanh cong");
        return "redirect:/admin/evaluate/list";
    }
}
