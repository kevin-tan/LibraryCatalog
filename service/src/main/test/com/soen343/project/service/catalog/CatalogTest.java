package com.soen343.project.service.catalog;

import com.soen343.project.repository.entity.catalog.item.Item;
import com.soen343.project.service.database.RecordDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class CatalogTest {

    private RecordDatabase recordDatabase;
    private Catalog catalog;
    private List<Item> expectedItems;
    private List<Item> items;

    @Before
    public void setup() {
/*        recordDatabase = Mockito.mock(RecordDatabase.class);
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
        }*/
    }

    @Test
    public void testUpdate_ItemEdited() {
/*        // Retrieve an item from the catalog inventory
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
        }*/
    }

    @Test
    public void testUpdate_ItemDeleted() {
/*

        // Setup expected
        expectedItems.remove(0);

        // Mocks
        when(recordDatabase.findAllItem()).thenReturn(items);
        Mockito.doAnswer((invocation) -> {
            items.remove(0);
            return null;
        }).when(recordDatabase).removeItem(anyLong());

        //Update catalog for the item
        catalog.deleteCatalogItem(1L);

        verify(recordDatabase,times(1)).removeItem(1L);

        for (int i = 0; i < catalog.getAllItem().size(); i++) {
            assertEquals(catalog.getAllItem().get(i).getId(), expectedItems.get(i).getId());
            assertEquals(catalog.getAllItem().get(i).getSpec().getId(), expectedItems.get(i).getSpec().getId());
        }
*/

    }

    @Test
    public void testUpdate_ItemAdded() {
/*        //Create new item to be added
        ItemSpecification spec = new Music(6L, "", null, "", "", "", "");
        Item item = new Item(6L, spec);
        //Setup expected
        expectedItems.add(item);

        //Mocks
        when(recordDatabase.findAllItem()).thenReturn(items);
        Mockito.doAnswer((invocation) -> {
            items.add(item);
            return null;
        }).when(recordDatabase).insertCatalogItem(item.getSpec());

        //Update catalog for the item
        catalog.addCatalogItem(spec);

        verify(recordDatabase,times(1)).insertCatalogItem(item.getSpec());

        for (int i = 0; i < catalog.getAllItem().size(); i++) {
            assertEquals(catalog.getAllItem().get(i).getId(), expectedItems.get(i).getId());
            assertEquals(catalog.getAllItem().get(i).getSpec().getId(), expectedItems.get(i).getSpec().getId());
        }*/
    }

    @Test
    public void testGetAllItem() {
/*
        for (int i = 0; i < catalog.getAllItem().size(); i++) {
            assertEquals(catalog.getAllItem().get(i).getId(), expectedItems.get(i).getId());
            assertEquals(catalog.getAllItem().get(i).getSpec().getId(), expectedItems.get(i).getSpec().getId());
        }*/
    }

}

