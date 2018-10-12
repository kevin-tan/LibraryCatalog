package com.soen343.project.repository.dao.catalog;

import com.soen343.project.repository.entity.catalog.Item;
import com.soen343.project.repository.entity.catalog.ItemSpecification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CatalogRepository {

    private static int itemIDs = 1;
    private static int itemSpecIDs = 1;
    private List<Item> items = new ArrayList<>();
    private List<ItemSpecification> itemSpecs = new ArrayList<>();

    public List<Item> findAll(){
        return items;
    }

    public Item findItem(long itemID){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getId() == itemID){
                return items.get(i);
            }
        }
        return null;
    }

    public ItemSpecification findItemSpec(ItemSpecification itemSpec){
        for(int i = 0; i < itemSpecs.size(); i++){
            if(itemSpecs.get(i).equals(itemSpec)){
                return itemSpecs.get(i);
            }
        }
        return null;
    }

    public void update(Item item, ItemSpecification itemSpec){
        ItemSpecification oldSpec = item.getSpec();
        item.setSpec(itemSpec);

        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getSpec() == oldSpec){
                return;
            }
        }
        for(int i = 0; i < itemSpecs.size(); i++){
            if(itemSpecs.get(i) == oldSpec){
                itemSpecs.remove(i);
                return;
            }
        }
    }

    public void addItemSpec(ItemSpecification itemSpec){
        itemSpec.setId(itemSpecIDs++);
        itemSpecs.add(itemSpec);
    }

    public void createItem(ItemSpecification itemSpec){
        items.add(new Item(itemIDs++, itemSpec));
    }

    public void remove(Item item){
        ItemSpecification spec = null;
        //Delete the item
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getId() == item.getId()){
                spec = items.get(i).getSpec();
                items.remove(i);
            }
        }
        //Checks if there are any items that references the spec of the deleted item
        for(int i = 0; i < items.size(); i++){
            if(spec == items.get(i).getSpec()){
                return;
            }
        }
        //If not, then delete the spec
        for(int i = 0; i < itemSpecs.size(); i++){
            if(spec == itemSpecs.get(i)){
                itemSpecs.remove(i);
            }
        }
    }
}
