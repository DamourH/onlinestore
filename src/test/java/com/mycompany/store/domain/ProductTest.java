package com.mycompany.store.domain;

import static com.mycompany.store.domain.ProductCategoryTestSamples.*;
import static com.mycompany.store.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.store.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void productCategoryTest() {
        Product product = getProductRandomSampleGenerator();
        ProductCategory productCategoryBack = getProductCategoryRandomSampleGenerator();

        product.addProductCategory(productCategoryBack);
        assertThat(product.getProductCategories()).containsOnly(productCategoryBack);
        assertThat(productCategoryBack.getProducts()).containsOnly(product);

        product.removeProductCategory(productCategoryBack);
        assertThat(product.getProductCategories()).doesNotContain(productCategoryBack);
        assertThat(productCategoryBack.getProducts()).doesNotContain(product);

        product.productCategories(new HashSet<>(Set.of(productCategoryBack)));
        assertThat(product.getProductCategories()).containsOnly(productCategoryBack);
        assertThat(productCategoryBack.getProducts()).containsOnly(product);

        product.setProductCategories(new HashSet<>());
        assertThat(product.getProductCategories()).doesNotContain(productCategoryBack);
        assertThat(productCategoryBack.getProducts()).doesNotContain(product);
    }
}
