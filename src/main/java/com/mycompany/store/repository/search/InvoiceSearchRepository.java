package com.mycompany.store.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.store.domain.Invoice;
import com.mycompany.store.repository.InvoiceRepository;
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
 * Spring Data Elasticsearch repository for the {@link Invoice} entity.
 */
public interface InvoiceSearchRepository extends ElasticsearchRepository<Invoice, Long>, InvoiceSearchRepositoryInternal {}

interface InvoiceSearchRepositoryInternal {
    Page<Invoice> search(String query, Pageable pageable);

    Page<Invoice> search(Query query);

    @Async
    void index(Invoice entity);

    @Async
    void deleteFromIndexById(Long id);
}

class InvoiceSearchRepositoryInternalImpl implements InvoiceSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InvoiceRepository repository;

    InvoiceSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, InvoiceRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Invoice> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Invoice> search(Query query) {
        SearchHits<Invoice> searchHits = elasticsearchTemplate.search(query, Invoice.class);
        List<Invoice> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Invoice entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Invoice.class);
    }
}
