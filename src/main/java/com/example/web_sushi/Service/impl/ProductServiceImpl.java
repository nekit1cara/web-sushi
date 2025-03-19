package com.example.web_sushi.Service.impl;

import com.example.web_sushi.Entity.ProductInfo;
import com.example.web_sushi.Entity.Products;
import com.example.web_sushi.Enums.ProductCategory;
import com.example.web_sushi.GlobalException.Exceptions.AlreadyExistException;
import com.example.web_sushi.GlobalException.Exceptions.NotFoundException;
import com.example.web_sushi.Repository.ProductInfoRepository;
import com.example.web_sushi.Repository.ProductsRepository;
import com.example.web_sushi.Service.interfaces.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductInfoRepository productInfoRepository;


    @Autowired
    public ProductServiceImpl(ProductsRepository productsRepository,
                              ProductInfoRepository productInfoRepository) {
        this.productsRepository = productsRepository;
        this.productInfoRepository = productInfoRepository;
    }


    @Override
    public ResponseEntity<?> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Products> products = productsRepository.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(products);

    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {

        Optional<Products> existingProduct = productsRepository.findById(productId);

            if (existingProduct.isEmpty()) {
                throw new NotFoundException("Product with id : " + productId + " not found");
            }

        Products foundedProduct = existingProduct.get();
            return ResponseEntity.status(HttpStatus.OK).body(foundedProduct);
    }

    @Override
    public ResponseEntity<?> getProductByName(String productName) {

        ProductInfo productInfoByName = productInfoRepository.findByProductName(productName);

            if (productInfoByName == null) {
                throw new NotFoundException("Product with name : " + productName + " not found");
            }

        return ResponseEntity.status(HttpStatus.OK).body(productInfoByName);

    }

    @Override
    public ResponseEntity<?> getProductsByCategory(int page, int size,ProductCategory category) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductInfo> sushi = productInfoRepository.findByProductCategory(ProductCategory.SUSHI, pageable);
        Page<ProductInfo> soup = productInfoRepository.findByProductCategory(ProductCategory.SOUP, pageable);
        Page<ProductInfo> wok = productInfoRepository.findByProductCategory(ProductCategory.WOK, pageable);
        Page<ProductInfo> mussels = productInfoRepository.findByProductCategory(ProductCategory.MUSSELS, pageable);
        Page<ProductInfo> vongole = productInfoRepository.findByProductCategory(ProductCategory.VONGOLE, pageable);
        Page<ProductInfo> snacks = productInfoRepository.findByProductCategory(ProductCategory.SNACKS, pageable);
        Page<ProductInfo> deserts = productInfoRepository.findByProductCategory(ProductCategory.DESERTS, pageable);
        Page<ProductInfo> drinks = productInfoRepository.findByProductCategory(ProductCategory.DRINKS, pageable);


        return switch (category) {

            case SUSHI -> ResponseEntity.status(HttpStatus.OK).body(sushi);
            case SOUP -> ResponseEntity.status(HttpStatus.OK).body(soup);
            case WOK -> ResponseEntity.status(HttpStatus.OK).body(wok);
            case MUSSELS -> ResponseEntity.status(HttpStatus.OK).body(mussels);
            case VONGOLE -> ResponseEntity.status(HttpStatus.OK).body(vongole);
            case SNACKS -> ResponseEntity.status(HttpStatus.OK).body(snacks);
            case DESERTS -> ResponseEntity.status(HttpStatus.OK).body(deserts);
            case DRINKS -> ResponseEntity.status(HttpStatus.OK).body(drinks);
        };
    }

    @Override
    @Transactional
    public ResponseEntity<?> createProduct(Products product) {

        createNewInfoForProduct(product);

        //Добавить доп соусы к заказу
        if (product.getInfo() == null) {
            throw new NotFoundException("Информация о продукте не указана.");
        }

            return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }



    @Override
    @Transactional
    public ResponseEntity<?> createProducts(List<Products> products) {

            for (Products product : products) {
                createNewInfoForProduct(product);
            }

        return ResponseEntity.status(HttpStatus.CREATED).body(products);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateProductById(Long id, Products product) {

       Optional<Products> existingProduct = productsRepository.findById(id);

            if (existingProduct.isEmpty()) {
                throw new NotFoundException("Product with id : " + id + " not found");
            }

       Products productToUpdate = existingProduct.get();

          if (product.getInfo().getProductName() != null) {
              productToUpdate.getInfo().setProductName(product.getInfo().getProductName());
          }
          if (product.getInfo().getProductDescription() != null) {
              productToUpdate.getInfo().setProductDescription(product.getInfo().getProductDescription());
          }
          if (product.getInfo().getProductCategory() != null) {
              productToUpdate.getInfo().setProductCategory(product.getInfo().getProductCategory());
          }
          if (product.getInfo().getProductWeight() != null) {
              productToUpdate.getInfo().setProductWeight(product.getInfo().getProductWeight());
          }
          if (product.getInfo().getProductPrice() != 0) {
              productToUpdate.getInfo().setProductPrice(product.getInfo().getProductPrice());
          }

       productsRepository.save(productToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(productToUpdate); //"Продукт:" + id +" успешно изменен."
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteProductById(Long id) {

        Optional<Products> existingProduct = productsRepository.findById(id);

            if (existingProduct.isEmpty()) {
                throw new NotFoundException("Product with id : " + id + " not found");
            }


        Products productToDelete = existingProduct.get();
        ProductInfo productInfo = productToDelete.getInfo();
            productInfoRepository.delete(productInfo);
            productsRepository.delete(productToDelete);
            return ResponseEntity.status(HttpStatus.OK).body("Продукт успешно удален.");
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

private void createNewInfoForProduct(Products product) {
    ProductInfo productInfo = new ProductInfo();
    productInfo.setProductName(product.getInfo().getProductName());
    productInfo.setProductCategory(product.getInfo().getProductCategory());
    productInfo.setProductPrice(product.getInfo().getProductPrice());
    productInfo.setProductDescription(product.getInfo().getProductDescription());
    productInfo.setProductWeight(product.getInfo().getProductWeight());

        if (productInfoRepository.existsByProductName(product.getInfo().getProductName())) {
            throw new AlreadyExistException("This product : " + product.getInfo().getProductName() + " already exist");
        }

    productInfoRepository.save(productInfo);
    product.setInfo(productInfo);
    productsRepository.save(product);
}


}
