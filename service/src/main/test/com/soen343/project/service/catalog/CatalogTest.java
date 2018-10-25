
import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import com.soen343.project.repository.entity.catalog.Music;
import com.soen343.project.service.catalog.Catalog;
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
    private ItemSpecification spec;


    @Before
    public void setup() {
        recordDatabase = Mockito.mock(RecordDatabase.class);
        catalog = new Catalog(recordDatabase);
        expectedItems= new LinkedList();
        for (long i = 1; i < 6; i++) {
            spec = new Music(i,"",null,"","","","");
            Item item = new Item(i, spec);
            expectedItems.add(item);
            catalog.addCatalogItem(spec);
        }
    }

    @Test
    public void testUpdate_ItemEdited(){
        Item item = new Item (1L,spec);
        expectedItems.set(0,item);
        catalog.editItem(1L,spec);
        assertThat(catalog.getAllItem(), is(expectedItems));
    }

    @Test
    public void testUpdate_ItemDeleted(){
        expectedItems.remove(0);
        catalog.deleteCatalogItem(1L);
        assertThat(catalog.getAllItem(), is(expectedItems));
    }

    @Test
    public void testUpdate_ItemAdded(){
        Item item = new Item (6L,spec);
        expectedItems.add(item);
        catalog.addCatalogItem(spec);
        assertThat(catalog.getAllItem(), is(expectedItems));
    }

    @Test
    public void testGetAllItem(){
        assertThat(catalog.getAllItem(), is(expectedItems));
   }

}

