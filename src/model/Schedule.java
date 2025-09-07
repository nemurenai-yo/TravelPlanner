package model;

import java.time.LocalDateTime;

public class Schedule {
    private String title;           
    private LocalDateTime dateTime; 
    private String category;        

    // カテゴリなしのコンストラクタ
    public Schedule(String title, LocalDateTime dateTime) {
        this.title = title;
        this.dateTime = dateTime;
        this.category = "未分類";
    }

    // カテゴリありのコンストラクタ
    public Schedule(String title, LocalDateTime dateTime, String category) {
        this.title = title;
        this.dateTime = dateTime;
        this.category = category != null && !category.isBlank() ? category : "未分類";
    }

    // getter / setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title != null && !title.isBlank()) this.title = title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        if(dateTime != null) this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if(category != null && !category.isBlank()) this.category = category;
    }
}
