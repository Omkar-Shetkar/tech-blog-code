package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Catalogue {

    @LlmTool(description = "List of items categorized by type, including code, name, and category")
    public List<Item> items() {
        return List.of(
                // Electronics
                new Item("ITM001", "Laptop Dell XPS", "Electronics"),
                new Item("ITM002", "Mouse Wireless", "Electronics"),
                new Item("ITM003", "Keyboard Mechanical", "Electronics"),
                new Item("ITM004", "Monitor 24in", "Electronics"),
                new Item("ITM005", "Headphones Sony", "Electronics"),
                new Item("ITM006", "USB Cable Type-C", "Electronics"),
                new Item("ITM007", "Hard Drive 1TB", "Electronics"),
                new Item("ITM008", "Webcam HD", "Electronics"),
                new Item("ITM009", "Power Adapter", "Electronics"),
                new Item("ITM019", "Apple iPhone", "Electronics"),

                // Bakery & Dairy
                new Item("ITM011", "Croissant Fresh Bakery", "Bakery"),
                new Item("ITM013", "Milk Full Cream 1L", "Dairy"),
                new Item("ITM014", "Butter Amul 500g", "Dairy"),
                new Item("ITM015", "Yogurt Plain 400g", "Dairy"),
                new Item("ITM017", "Bread White Loaf", "Bakery"),
                new Item("ITM018", "Cheese Slice Pack", "Dairy"),

                // Snacks & Confectionery
                new Item("ITM010", "Chocolate Bar Dairy Milk", "Snacks"),
                new Item("ITM012", "Marie Biscuits Pack", "Snacks"),
                new Item("ITM016", "Cookies Oreo Pack", "Snacks"),
                new Item("ITM020", "Ice Cream Vanilla Tub", "Snacks")
        );
    }
}
