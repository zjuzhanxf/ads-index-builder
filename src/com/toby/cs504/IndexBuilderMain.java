package com.toby.cs504;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IndexBuilderMain {

    private static final String mMemcachedServer = "127.0.0.1";
    private static final int mMemcachedPortal = 11211;
    private static final String mysql_host = "127.0.0.1:3306";
    private static final String mysql_db = "search_ads_homework";
    private static final String mysql_user = "root";
    private static final String mysql_pass = "password";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: IndexBuilder <adsDataFilePath> <budgetDataFilePath>");
            System.exit(0);
        }

        String mAdsDataFilePath = args[0];
        String mBudgetFilePath = args[1];

        IndexBuilder indexBuilder = new IndexBuilder(mMemcachedServer,mMemcachedPortal,mysql_host,mysql_db,mysql_user,mysql_pass);

        try (BufferedReader brAd = new BufferedReader(new FileReader(mAdsDataFilePath))) {
            String line;
            while ((line = brAd.readLine()) != null) {
                JSONObject adJson = new JSONObject(line);
                Ad ad = new Ad();
                if(adJson.isNull("adId") || adJson.isNull("campaignId")) {
                    continue;
                }
                ad.adId = adJson.getLong("adId");
                ad.campaignId = adJson.getLong("campaignId");
                ad.brand = adJson.isNull("brand") ? "" : adJson.getString("brand");
                ad.price = adJson.isNull("price") ? 100.0 : adJson.getDouble("price");
                ad.thumbnail = adJson.isNull("thumbnail") ? "" : adJson.getString("thumbnail");
                ad.title = adJson.isNull("title") ? "" : adJson.getString("title");
                ad.detail_url = adJson.isNull("detail_url") ? "" : adJson.getString("detail_url");
                ad.bidPrice = adJson.isNull("bidPrice") ? 1.0 : adJson.getDouble("bidPrice");
                ad.pClick = adJson.isNull("pClick") ? 0.0 : adJson.getDouble("pClick");
                ad.category =  adJson.isNull("category") ? "" : adJson.getString("category");
                ad.description = adJson.isNull("description") ? "" : adJson.getString("description");
                ad.keyWords = new ArrayList<String>();
                JSONArray keyWords = adJson.isNull("keyWords") ? null :  adJson.getJSONArray("keyWords");
                for(int j = 0; j < keyWords.length();j++)
                {
                    ad.keyWords.add(keyWords.getString(j));
                }

				if(!indexBuilder.buildInvertIndex(ad)) {
					System.out.println("ERROR! Failed to insert ad into Memcached");
				}
                if (!indexBuilder.buildForwardIndex(ad)) {
                    System.out.println("ERROR! Failed to insert ad into DB");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //load budget data
        try (BufferedReader brBudget = new BufferedReader(new FileReader(mBudgetFilePath))) {
            String line;
            while ((line = brBudget.readLine()) != null) {
                JSONObject campaignJson = new JSONObject(line);
                Long campaignId = campaignJson.getLong("campaignId");
                double budget = campaignJson.getDouble("budget");
                Campaign camp = new Campaign();
                camp.campaignId = campaignId;
                camp.budget = budget;
                if(!indexBuilder.updateBudget(camp))
                {
                    //log
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        indexBuilder.Close();

        return;
    }

}
