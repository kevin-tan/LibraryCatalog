package com.soen343.project.repository.dao.catalog.item.com;

import com.soen343.project.repository.dao.Gateway;

import java.util.List;

/**
 * Created by Kevin Tan 2018-11-21
 */

public interface ItemsGateway<E> extends Gateway<E> {
    List<E> findByItemSpecId(String itemType, Long itemSpecId);

    E findFirstByItemSpecId(String itemType, Long itemSpecId);
}
