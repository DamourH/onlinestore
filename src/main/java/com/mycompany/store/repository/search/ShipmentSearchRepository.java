package com.mycompany.store.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.store.domain.Shipment;
import com.mycompany.store.repository.ShipmentRepository;
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
 * Spring Data Elasticsearch repository for the {@link Shipment} entity.
 */
public interface ShipmentSearchRepository extends ElasticsearchRepository<Shipment, Long>, ShipmentSearchRepositoryInternal {}

interface ShipmentSearchRepositoryInternal {
    Page<Shipment> search(String query, Pageable pageable);

    Page<Shipment> search(Query query);

    @Async
    void index(Shipment entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ShipmentSearchRepositoryInternalImpl implements ShipmentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ShipmentRepository repository;

    ShipmentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ShipmentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Shipment> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Shipment> search(Query query) {
        SearchHits<Shipment> searchHits = elasticsearchTemplate.search(query, Shipment.class);
        List<Shipment> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Shipment entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Shipment.class);
    }
}
