package com.soen343.project.service.registry;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.service.database.RecordDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CatalogTest {

    private RecordDatabase recordDatabase;
    private Catalog catalog;
    private List <Item> expectedItems;


    @Before
    public void setup() {
        recordDatabase = Mockito.mock(RecordDatabase.class);
        catalog = new Catalog(recordDatabase);
        expectedItems= new LinkedList();
        for (long i = 1; i < 6; i++) {
        Item item = new Item(i);
        catalog.item.add(item);
        expectedItems.add(item);
        }
    }

    @Test
    public void testUpdate_ItemEdited(){
        Item item = new Item ("3","");
        expectedItems.update(1);
        catalog.update(item);
        assertThat(catalog.item, is(expectedItems));
    }

    @Test
    public void testUpdate_ItemDeleted(){
        Item item = new Item ("1","");
        expectedItems.remove(0);
        catalog.update(item);
        assertThat(catalog.item, is(expectedItems));
    }

    @Test
    public void testUpdate_ItemAdded(){
        Item item = new Item ("8","");
        expectedItems.add(new Item(8));
        catalog.update(item);
        assertThat(catalog.item, is(expectedItems));
    }
    
    @Test
    public void testGetAllItem(){
        assertThat(catalog.getAllItem(), is(expectedItems))
    }

    }

