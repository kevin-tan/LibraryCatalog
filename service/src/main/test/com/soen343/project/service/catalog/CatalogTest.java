package com.soen343.project.service.catalog;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.catalog.Music;
import com.soen343.project.service.database.RecordDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CatalogTest {

    private RecordDatabase recordDatabase;
    private Catalog catalog;
    private List<Item> expectedItems;
    private List<Item> items;

    @Before
    public void setup() {
        recordDatabase = Mockito.mock(RecordDatabase.class);
        catalog = new Catalog(recordDatabase);
        expectedItems = new LinkedList<>();
        items = new LinkedList<>();
        for (long i = 1; i < 6; i++) {
            ItemSpecification spec = new Music(i, "", null, "", "", "", "");
            ItemSpecification spec2 = new Music(i, "", null, "", "", "", "");

            Item item = new Item(i, spec);
            expectedItems.add(item);
            Item item2 = new Item(i, spec2);
            items.add(item2);

            catalog.addCatalogItem(spec);
        }
    }

    @Test
    public void testUpdate_ItemEdited() {
        // Retrieve an item from the catalog inventory
        Item item = expectedItems.get(0);
        // Modify spec for item
        item.getSpec().setTitle("New");
        // Setup expected
        expectedItems.set(0, item);

        //Mocks
        when(recordDatabase.findAllItem()).thenReturn(items);
        Mockito.doAnswer((invocation) -> {
            items.set(0, item);
            return null;
        }).when(recordDatabase).updateItem(anyLong(), any());

        // Update catalog for the item
        catalog.editItem(item.getId(), item.getSpec());

        for (int i = 0; i < catalog.getAllItem().size(); i++) {
            assertEquals(catalog.getAllItem().get(0).getId(), expectedItems.get(0).getId());
            assertEquals(catalog.getAllItem().get(0).getSpec().getId(), expectedItems.get(0).getSpec().getId());
            assertEquals(catalog.getAllItem().get(0).getSpec().getTitle(), expectedItems.get(0).getSpec().getTitle());
        }
    }

    @Test
    public void testUpdate_ItemDeleted() {
        expectedItems.remove(0);
        catalog.deleteCatalogItem(1L);
        assertThat(catalog.getAllItem(), is(expectedItems));
    }

    @Test
    public void testUpdate_ItemAdded() {
//        Item item = new Item(6L, spec);
//        expectedItems.add(item);
//        catalog.addCatalogItem(spec);
        assertThat(catalog.getAllItem(), is(expectedItems));
    }

    @Test
    public void testGetAllItem() {
        assertThat(catalog.getAllItem(), is(expectedItems));
    }

}

