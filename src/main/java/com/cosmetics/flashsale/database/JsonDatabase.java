package com.cosmetics.flashsale.database;

import com.cosmetics.flashsale.entity.FlashSaleCampaign;
import com.cosmetics.flashsale.entity.FlashSaleInventory;
import com.cosmetics.flashsale.entity.SaleAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * LỚP CƠ SỞ DỮ LIỆU (DATABASE): JsonDatabase
 * Mục đích: Đóng vai trò là Kho lưu trữ dữ liệu tập trung.
 * - Đọc dữ liệu từ file JSON khi khởi động.
 * - Lưu trữ dữ liệu trong RAM (In-Memory).
 * - Không tự động lưu lại khi tắt máy (Volatile).
 * =======================================================
 */
public class JsonDatabase {
    private static JsonDatabase instance;
    private List<FlashSaleInventory> inventories = new ArrayList<>();
    private List<FlashSaleCampaign> campaigns = new ArrayList<>();
    private SaleAnalytics analytics;

    private JsonDatabase() {
        loadInitialData();
    }

    public static synchronized JsonDatabase getInstance() {
        if (instance == null) {
            instance = new JsonDatabase();
        }
        return instance;
    }

    private void loadInitialData() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("initial_data.json")) {
            if (is == null) return;
            
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(new InputStreamReader(is), JsonObject.class);

            // Nạp Tồn kho
            if (root.has("products")) {
                JsonArray prods = root.getAsJsonArray("products");
                for (JsonElement e : prods) {
                    JsonObject p = e.getAsJsonObject();
                    inventories.add(new FlashSaleInventory(p.get("name").getAsString(), p.get("quantity").getAsInt()));
                }
            }

            // Nạp Chiến dịch
            if (root.has("campaigns")) {
                JsonArray cams = root.getAsJsonArray("campaigns");
                for (JsonElement e : cams) {
                    JsonObject c = e.getAsJsonObject();
                    LocalDateTime start = LocalDateTime.now().plusHours(c.get("hoursFromNowStart").getAsLong());
                    LocalDateTime end = LocalDateTime.now().plusHours(c.get("hoursFromNowEnd").getAsLong());
                    campaigns.add(new FlashSaleCampaign(start, end, c.get("discountPercent").getAsDouble()));
                }
            }

            // Nạp Analytics
            if (root.has("analytics")) {
                JsonObject a = root.getAsJsonObject("analytics");
                analytics = new SaleAnalytics(a.get("totalInitialInventory").getAsInt());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FlashSaleInventory> getInventories() {
        return inventories;
    }

    public List<FlashSaleCampaign> getCampaigns() {
        return campaigns;
    }

    public SaleAnalytics getAnalytics() {
        return analytics;
    }
}
