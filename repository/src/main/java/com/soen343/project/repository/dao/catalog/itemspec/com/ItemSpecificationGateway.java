package com.soen343.project.repository.dao.catalog.itemspec.com;

import com.soen343.project.repository.dao.Gateway;

import java.util.List;

public interface ItemSpecificationGateway<E> extends Gateway<E> {
    List<E> findByTitle(String title);
}
