package com.soen343.project.repository.entity.catalog;

public abstract class PrintedItem extends ItemSpecification{

    private String publisher;
    private String pubDate;
    private String lang;
    private String isbn10;
    private String isbn13;

    public PrintedItem(long id, String title, String publisher, String pubDate,
                       String lang, String isbn10, String isbn13){
        super(id, title);
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.lang = lang;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
    }

    public boolean isEqual(PrintedItem printedItem){
        return super.isEqual(printedItem) && publisher.equals(printedItem.publisher) && pubDate.equals(printedItem.pubDate) &&
                lang.equals(printedItem.lang) && isbn10.equals(printedItem.isbn10) && isbn13.equals(printedItem.isbn13);
    }
}
