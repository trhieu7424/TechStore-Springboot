package com.example.baitap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.baitap.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 1. Tìm theo tên 
    List<Product> findByNameContainingIgnoreCase(String name);
    // 2. Tìm theo khoảng giá
    List<Product> findByPriceBetween(Double min, Double max);
    // 3. Tìm sản phẩm có số lượng lớn hơn x
    List<Product> findByQuantityGreaterThan(Integer quantity);
    // 4. Tìm theo tên và giá
    List<Product> findByNameAndPrice(String name, Double price);
    // 5. Đếm sản phẩm theo giá
    long countByPriceLessThan(Double price);

    //JPQL
    // 1. JOIN: Lấy sản phẩm thuộc danh mục có tên cụ thể
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    // 2. Tính toán: Lấy danh sách sản phẩm có giá cao hơn giá trung bình
    @Query("SELECT p FROM Product p WHERE p.price > (SELECT AVG(p2.price) FROM Product p2)")
    List<Product> findProductsExpensiveThanAverage();

    // 3. Thống kê: Đếm số lượng sản phẩm trong mỗi danh mục (trả về Object[])
    @Query("SELECT c.name, COUNT(p) FROM Category c LEFT JOIN c.products p GROUP BY c.name")
    List<Object[]> countProductsByCategory();
    
    @Query("SELECT p FROM Product p WHERE p.category.name = :catName AND p.price >= :minPrice")
    List<Product> findByCategoryNameAndPrice(@Param("catName") String catName, @Param("minPrice") Double minPrice);
    
}
