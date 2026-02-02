package Controller;
import com.example.bai4.model.Product;
import com.example.bai4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // ===== HÀM LƯU FILE ẢNH DÙNG CHUNG =====
    private String saveImage(MultipartFile file) throws IOException {

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        // LẤY ĐUÔI FILE (.jpg, .png…)
        String originalName = file.getOriginalFilename();
        String extension = "";

        int i = originalName.lastIndexOf('.');
        if (i > 0) {
            extension = originalName.substring(i); // .jpg
        }

        // TẠO TÊN MỚI NGẮN GỌN
        String fileName = UUID.randomUUID().toString() + extension;

        File saveFile = new File(uploadDir + fileName);
        file.transferTo(saveFile);

        return "/uploads/" + fileName;
    }



    // ===== DANH SÁCH =====
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", service.getAll());
        return "products";
    }

    // ===== FORM THÊM =====
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    // ===== XỬ LÝ THÊM =====
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("product") Product product,
                      BindingResult result,
                      Model model) throws IOException {

        MultipartFile file = product.getImageFile();

        // Nếu có file mới → lưu ảnh
        if (file != null && !file.isEmpty()) {
            String imageUrl = saveImage(file);
            product.setImageUrl(imageUrl);
        }

        // ❗ Nếu KHÔNG có file mới VÀ cũng chưa có ảnh nào trước đó → báo lỗi
        if ((file == null || file.isEmpty()) && product.getImageUrl() == null) {
            result.rejectValue("imageFile", "error.product", "Vui lòng chọn hình ảnh");
        }

        // Nếu có lỗi khác (name, price...)
        if (result.hasErrors()) {
            model.addAttribute("product", product); // giữ lại imageUrl để hiển thị lại
            return "add-product";
        }

        service.add(product);
        return "redirect:/products";
    }



    // ===== FORM SỬA =====
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.getById(id));
        return "edit-product";
    }

    // ===== XỬ LÝ SỬA =====
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute Product product,
                       BindingResult result) throws IOException {

        Product oldProduct = service.getById(product.getId());
        MultipartFile file = product.getImageFile();

        if (result.hasErrors()) {
            return "edit-product";
        }

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveImage(file);
            product.setImageUrl(imageUrl);
        } else {
            product.setImageUrl(oldProduct.getImageUrl());
        }

        service.update(product);
        return "redirect:/products";
    }

    // ===== XÓA =====
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/products";
    }
}
