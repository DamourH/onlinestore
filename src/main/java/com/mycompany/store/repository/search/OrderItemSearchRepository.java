package com.mycompany.store.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.store.domain.OrderItem;
import com.mycompany.store.repository.OrderItemRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link OrderItem} entity.
 */
public interface OrderItemSearchRepository extends ElasticsearchRepository<OrderItem, Long>, OrderItemSearchRepositoryInternal {}

interface OrderItemSearchRepositoryInternal {
    Page<OrderItem> search(String query, Pageable pageable);

    Page<OrderItem> search(Query query);

    @Async
    void index(OrderItem entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OrderItemSearchRepositoryInternalImpl implements OrderItemSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OrderItemRepository repository;

    OrderItemSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OrderItemRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OrderItem> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OrderItem> search(Query query) {
        SearchHits<OrderItem> searchHits = elasticsearchTemplate.search(query, OrderItem.class);
        List<OrderItem> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OrderItem entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), OrderItem.class);
    }
}
