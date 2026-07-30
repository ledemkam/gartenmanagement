package de0.backend.gartenmanagement.services;

import de0.backend.gartenmanagement.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CrudServices<I, O, ID>{

        // I for request, O for response
        void create(final I request);

        void update(final ID id, final I request);

        PageResponse<O> findAll(final Pageable pageable);

        O findById(final ID id);

        void delete(final ID id);

}
