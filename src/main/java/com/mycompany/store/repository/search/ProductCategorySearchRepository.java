package com.mycompany.store.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.store.domain.ProductCategory;
import com.mycompany.store.repository.ProductCategoryRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductCategory} entity.
 */
public interface ProductCategorySearchRepository
    extends ElasticsearchRepository<ProductCategory, Long>, ProductCategorySearchRepositoryInternal {}

interface ProductCategorySearchRepositoryInternal {
    Stream<ProductCategory> search(String query);

    Stream<ProductCategory> search(Query query);

    @Async
    void index(ProductCategory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductCategorySearchRepositoryInternalImpl implements ProductCategorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductCategoryRepository repository;

    ProductCategorySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductCategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductCategory> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductCategory> search(Query query) {
        return elasticsearchTemplate.search(query, ProductCategory.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductCategory entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductCategory.class);
    }
}
