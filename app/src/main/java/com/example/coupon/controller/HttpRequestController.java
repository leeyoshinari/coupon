package com.example.coupon.controller;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ExpandableListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpRequestController {
    private static String TB_URL = "https://eco.taobao.com/router/rest";        // 淘宝联盟 url
    private static String TB_APP_SECRET = "ad114d7bdaef1534e3d4b10837b1066b";       // 应用AppSecret
    private static String TB_APP_KEY = "32482043";      // 应用app_key
    private static String TB_PID_LAST = "111189250370";     // 推广位PID最后一位
    private static String JD_URL = "https://router.jd.com/api";     // 京东官方 url
    private static String JD_APP_KEY = "3496742e2d5a2fde480d356ec922da1e";
    private static String JD_APP_SECRET = "3046bbee1ede4d3583943cbd33c73bf4";
    private static String PDD_URL = "https://gw-api.pinduoduo.com/api/router";       // 多多进宝 url
    private static String PDD_SECRET = "2440d8f47d626bb837e4ea3f2920d3966ec37726";  // 拼多多应用client_secret
    private static String PDD_CLIENT_ID = "ed3db9f07b2a4476bddb07a223c6d68e";   // 拼多多应用client_id

    // 拼多多推广位PID
    private static String PDD_PID = "15084399_190061927";
    private static String PDD_custom_parameters = "{\"uid\":\"15084399_190061927\"}";

    private String platform = "tb";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return this.platform;
    }

    public String generateUrlPathForList(String searchKey, int pageNo) {
        JSONObject params = new JSONObject();
        try {
            if (getPlatform().equals("tb")) {
                params.put("format", "JSON");
                params.put("sign_method", "md5");
                params.put("app_key", TB_APP_KEY);
                params.put("adzone_id", TB_PID_LAST);
                params.put("timestamp", dateFormat.format(new Date())); // System.currentTimeMillis() + 28800000
                params.put("method", "taobao.tbk.dg.material.optional");
                params.put("v", "2.0");
                params.put("platform", 2);
                params.put("page_no", pageNo);
                params.put("q", searchKey);
                params.put("sign", encryptParams(params, TB_APP_SECRET));
            }
            if (getPlatform().equals("jd")) {
                return queryGoodsListForJd(searchKey, pageNo);
            }
            if (getPlatform().equals("pdd")) {
                params.put("client_id", PDD_CLIENT_ID);
                params.put("pid", PDD_PID);
                params.put("data_type", "JSON");
                params.put("sort_type", 0);
                params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
                params.put("type", "pdd.ddk.goods.search");
                params.put("page", pageNo);
                params.put("custom_parameters", PDD_custom_parameters);
                params.put("keyword", searchKey);
                params.put("sign", encryptParams(params, PDD_SECRET));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUrlByPlatform(params);
    }

    public String generateUrlPathForDetail(HashMap<String, Object> hashMap) {
        JSONObject params = new JSONObject();
        try {
            if (getPlatform().equals("jd")) {
                params.put("app_key", JD_APP_KEY);
                params.put("method", "jd.union.open.goods.bigfield.query");
                params.put("format", "JSON");
                params.put("timestamp", dateFormat.format(new Date()));
                params.put("v", "1.0");
                params.put("sign_method", "md5");
                params.put("param_json", "{\"goodsReq\": {\"skuIds\": [\"" + hashMap.get("sku_id") + "\"]}}");
                params.put("sign", encryptParams(params, JD_APP_SECRET));
            }
            if (getPlatform().equals("pdd")) {
                params.put("client_id", PDD_CLIENT_ID);
                params.put("pid", PDD_PID);
                params.put("data_type", "JSON");
                params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
                params.put("type", "pdd.ddk.goods.detail");
                params.put("goods_sign", hashMap.get("goods_sign"));
                params.put("search_id", hashMap.get("search_id"));
                params.put("custom_parameters", PDD_custom_parameters);
                params.put("sign", encryptParams(params, PDD_SECRET));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUrlByPlatform(params);
    }

    public JSONObject generatePromotion(JSONObject queryParam, boolean isApp) {
        JSONObject params = new JSONObject();
        try {
            if (getPlatform().equals("jd")) {
                return httpRequestGet(queryGeneratePromotionForJd(queryParam.getString("goods_id")));
            }
            if (getPlatform().equals("pdd")) {
                params.put("client_id", PDD_CLIENT_ID);
                params.put("p_id", PDD_PID);
                params.put("data_type", "JSON");
                params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000));
                params.put("type", "pdd.ddk.goods.promotion.url.generate");
                params.put("goods_sign_list", "[\"" + queryParam.getString("goods_sign") + "\"]");
                params.put("search_id", queryParam.getString("search_id"));
                params.put("custom_parameters", PDD_custom_parameters);
                if (isApp) {
                    params.put("generate_schema_url", true);
                } else {
                    params.put("generate_we_app", true);
                }
                params.put("sign", encryptParams(params, PDD_SECRET));
                return httpRequestGet(getUrlByPlatform(params));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public String convertJsonToUrlParams(JSONObject params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Iterator<String> keys = params.keys();
            while (keys.hasNext()) {
                String k = keys.next();
                try {
                    if (k.equals("custom_parameters") || k.equals("param_json")) {
                        stringBuilder.append(k).append("=").append(URLEncoder.encode(params.getString(k), "UTF-8"));
                    } else {
                        stringBuilder.append(k).append("=").append(params.getString(k));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (keys.hasNext()) {
                    stringBuilder.append("&");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String encryptParams(JSONObject params, String secret) {
        try {
            Iterator<String> keys = params.keys();
            List<String> keysList = new ArrayList<>();
            keys.forEachRemaining(keysList::add);
            keysList.sort(Comparator.naturalOrder());
            StringBuilder paramString = new StringBuilder(secret);
            for (String k : keysList) {
                paramString.append(k).append(params.getString(k));
            }
            paramString.append(secret);
            return stringToMd5(paramString.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<HashMap<String, Object>> parseGoodList(String urlPath) {
        List<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            JSONObject result = httpRequestGet(urlPath);
            if (getPlatform().equals("tb")) {
                arrayList = parseTbGoodList(result);
            }
            if (getPlatform().equals("jd")) {
                arrayList = parseJdGoodList(result);
            }
            if (getPlatform().equals("pdd")) {
                arrayList = parsePddGoodList(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public JSONObject parseGoodDetail(String urlPath) {
        JSONObject goodDetail = null;
        try {
            JSONObject result = httpRequestGet(urlPath);
            if (getPlatform().equals("tb")) {
                goodDetail = parseTbGoodDetail(result);
            }
            if (getPlatform().equals("jd")) {
                goodDetail = parseJdGoodDetail(result);
            }
            if (getPlatform().equals("pdd")) {
                goodDetail = parsePddGoodDetail(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodDetail;
    }

    public JSONObject httpRequestGet(String urlPath) {
        JSONObject result = null;
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(9000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            result = new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    public Bitmap getImageBitmap(String url) {
        Bitmap bitmap = null;
        HttpsURLConnection conn = null;
        try {
            URL imgUrl = new URL(url);
            conn = (HttpsURLConnection) imgUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setDoInput(true);
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }

    public String stringToMd5(String inputStr) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] data = inputStr.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = messageDigest.digest(data);
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hex.toUpperCase());
        }
        return stringBuilder.toString();
    }

    public String getUrlByPlatform(JSONObject params) {
        String urlPath = null;
        switch (getPlatform()) {
            case "tb":
                urlPath = TB_URL + "?" + convertJsonToUrlParams(params);
                break;
            case "jd":
                urlPath = JD_URL + "?" + convertJsonToUrlParams(params);
                break;
            case "pdd":
                urlPath = PDD_URL + "?" + convertJsonToUrlParams(params);
                break;
        }
        return urlPath;
    }

    public String queryGoodsListForJd(String searchKey, int pageNo) {
        String urlPath = null;
        try {
            JSONObject params = new JSONObject();
            params.put("apikey", "25ee321ae0f7f9be");
            params.put("pageindex", pageNo);
            params.put("keyword", searchKey);
            params.put("sort", "");
            params.put("sortname", "");
            params.put("ispg", 0);
            params.put("iscoupon", 0);
            params.put("isunion", "1");
            urlPath = "https://api-gw.haojingke.com/index.php/v1/api/jd/goodslist?" + convertJsonToUrlParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlPath;
    }

    public String queryGeneratePromotionForJd(String skuId) {
        String urlPath = null;
        try {
            JSONObject params = new JSONObject();
            params.put("apikey", "25ee321ae0f7f9be");
            params.put("unionId", "1002712393");
            params.put("type", 1);
            params.put("positionid", "3003427429");
            params.put("goods_id", skuId);
            urlPath = "https://api-gw.haojingke.com/index.php/v1/api/jd/getunionurl?" + convertJsonToUrlParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlPath;
    }

    @SuppressLint("DefaultLocale")
    public List<HashMap<String, Object>> parseTbGoodList(JSONObject goodObj) {
        List<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            goodObj.getJSONObject("tbk_dg_material_optional_response");
            goodObj.getJSONObject("tbk_dg_material_optional_response").getJSONObject("result_list");
            JSONArray goodsList = goodObj.getJSONObject("tbk_dg_material_optional_response").getJSONObject("result_list").getJSONArray("map_data");
            HashMap<String, Object> hashMap = new HashMap<>();
            for (int i = 0; i < goodsList.length(); i++) {
                hashMap = new HashMap<>();
                hashMap.put("img", ""); // pict_url
                hashMap.put("title_img", goodsList.getJSONObject(i).getJSONObject("small_images").getJSONArray("string"));
                hashMap.put("item_id", goodsList.getJSONObject(i).getString("item_id"));
                hashMap.put("title", goodsList.getJSONObject(i).getString("title"));
                hashMap.put("shop", goodsList.getJSONObject(i).getString("shop_title"));
                int volume = goodsList.getJSONObject(i).getInt("volume");
                String volumeText = String.valueOf(volume);
                if (volume > 9999) {
                    volumeText = (int) volume / 10000 + "万";
                } else if (volume > 999) {
                    volumeText = (int) volume / 1000 + "千";
                }
                if (goodsList.getJSONObject(i).has("coupon_amount") && goodsList.getJSONObject(i).getDouble("coupon_amount") > 0) {
                    hashMap.put("coupon_url", goodsList.getJSONObject(i).getString("coupon_share_url"));
                    hashMap.put("coupon_price", goodsList.getJSONObject(i).getString("coupon_amount"));
                    hashMap.put("coupon_text", "元优惠券   " + volumeText + "人已购买");
                    hashMap.put("sale_price", goodsList.getJSONObject(i).getString("zk_final_price"));
                    hashMap.put("final_price_text", " 券后 ￥");
                    hashMap.put("final_price", String.format("%.2f", goodsList.getJSONObject(i).getDouble("zk_final_price") - goodsList.getJSONObject(i).getDouble("coupon_amount")));
                } else {
                    hashMap.put("item_url", goodsList.getJSONObject(i).getString("url"));
                    hashMap.put("coupon_price", "");
                    hashMap.put("coupon_text", volumeText + "人已购买");
                    hashMap.put("sale_price", "");
                    hashMap.put("final_price_text", "");
                    hashMap.put("final_price", goodsList.getJSONObject(i).getString("zk_final_price"));
                }
                arrayList.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressLint("DefaultLocale")
    public List<HashMap<String, Object>> parseJdGoodList(JSONObject goodObj) {
        List<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            goodObj.getJSONObject("data");
            JSONArray goodsList = goodObj.getJSONObject("data").getJSONArray("data");
            HashMap<String, Object> hashMap = new HashMap<>();
            for (int i = 0; i < goodsList.length(); i++) {
                hashMap = new HashMap<>();
                hashMap.put("skuId", goodsList.getJSONObject(i).getString("skuId"));
                hashMap.put("img", ""); //goodsList.getJSONObject(i).getJSONObject("imageInfo").getJSONArray("imageList").getJSONObject(0).getDouble("url")
                hashMap.put("title", goodsList.getJSONObject(i).getString("skuName"));
                hashMap.put("shop", goodsList.getJSONObject(i).getJSONObject("shopInfo").getString("shopName"));
                int volume = goodsList.getJSONObject(i).getInt("inOrderCount30Days");
                String volumeText = String.valueOf(volume);
                if (volume > 9999) {
                    volumeText = (int) volume / 10000 + "万";
                } else if (volume > 999) {
                    volumeText = (int) volume / 1000 + "千";
                }
                if (goodsList.getJSONObject(i).has("couponInfo") && goodsList.getJSONObject(i).getJSONObject("couponInfo").getJSONArray("couponList").length() > 0 &&
                        goodsList.getJSONObject(i).getJSONObject("couponInfo").getJSONArray("couponList").getJSONObject(0).getDouble("quota") < goodsList.getJSONObject(i).getJSONObject("priceInfo").getDouble("price")) {
                    hashMap.put("coupon_price", String.format("%.2f", goodsList.getJSONObject(i).getJSONObject("couponInfo").getJSONArray("couponList").getJSONObject(0).getDouble("discount")));
                    hashMap.put("coupon_text", "元优惠券   " + volumeText + "人已购买");
                    hashMap.put("sale_price", goodsList.getJSONObject(i).getJSONObject("priceInfo").getDouble("price"));
                    hashMap.put("final_price_text", " 券后 ￥");
                    hashMap.put("final_price", String.format("%.2f", (goodsList.getJSONObject(i).getJSONObject("priceInfo").getDouble("price") - goodsList.getJSONObject(i).getJSONObject("couponInfo").getJSONArray("couponList").getJSONObject(0).getDouble("discount"))));
                } else {
                    hashMap.put("coupon_price", "");
                    hashMap.put("coupon_text", volumeText + "人已购买");
                    hashMap.put("sale_price", "");
                    hashMap.put("final_price_text", "");
                    hashMap.put("final_price", String.format("%.2f", goodsList.getJSONObject(i).getJSONObject("priceInfo").getDouble("price")));
                }
                arrayList.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressLint("DefaultLocale")
    public List<HashMap<String, Object>> parsePddGoodList(JSONObject goodObj) {
        List<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            goodObj.getJSONObject("goods_search_response");
            JSONArray goodsList = goodObj.getJSONObject("goods_search_response").getJSONArray("goods_list");
            HashMap<String, Object> hashMap = new HashMap<>();
            for (int i = 0; i < goodsList.length(); i++) {
                hashMap = new HashMap<>();
                hashMap.put("img", ""); //goods_thumbnail_url
                hashMap.put("title", goodsList.getJSONObject(i).getString("goods_name"));
                hashMap.put("shop", goodsList.getJSONObject(i).getString("mall_name"));
                hashMap.put("goods_sign", goodsList.getJSONObject(i).getString("goods_sign"));
                hashMap.put("search_id", goodsList.getJSONObject(i).getString("search_id"));
                if (goodsList.getJSONObject(i).has("coupon_discount") && goodsList.getJSONObject(i).getInt("coupon_discount") > 0) {
                    hashMap.put("coupon_price", String.format("%.2f", goodsList.getJSONObject(i).getDouble("coupon_discount") / 100));
                    hashMap.put("coupon_text", "元优惠券   " + goodsList.getJSONObject(i).getString("sales_tip") + "人已购买");
                    hashMap.put("sale_price", String.format("%.2f", goodsList.getJSONObject(i).getDouble("min_group_price") / 100));
                    hashMap.put("final_price_text", " 券后 ￥");
                    hashMap.put("final_price", String.format("%.2f", (goodsList.getJSONObject(i).getDouble("min_group_price") - goodsList.getJSONObject(i).getDouble("coupon_discount")) / 100));
                } else {
                    hashMap.put("coupon_price", "");
                    hashMap.put("coupon_text", goodsList.getJSONObject(i).getString("sales_tip") + "人已购买");
                    hashMap.put("sale_price", "");
                    hashMap.put("final_price_text", "");
                    hashMap.put("final_price", String.format("%.2f", goodsList.getJSONObject(i).getDouble("min_group_price") / 100));
                }
                arrayList.add(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressLint("DefaultLocale")
    public JSONObject parseTbGoodDetail(JSONObject goodObj) {
        JSONObject goodDetail = new JSONObject();
        try {
            goodObj.getJSONObject("tbk_item_info_get_response").getString("results");
            JSONObject detail = new JSONObject(goodObj.getJSONObject("tbk_item_info_get_response").getString("results")).getJSONArray("n_tbk_item").getJSONObject(0);
            goodDetail.put("title_img", detail.getJSONObject("small_images").getJSONArray("string"));
            goodDetail.put("provcity", detail.getString("provcity"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodDetail;
    }

    @SuppressLint("DefaultLocale")
    public JSONObject parseJdGoodDetail(JSONObject goodObj) {
        JSONObject goodDetail = new JSONObject();
        try {
            goodObj.getJSONObject("jd_union_open_goods_bigfield_query_response").getString("result");
            JSONObject detail = new JSONObject(goodObj.getJSONObject("jd_union_open_goods_bigfield_query_response").getString("result")).getJSONArray("data").getJSONObject(0);
            goodDetail.put("detail_desc", detail.getString("detailImages").split(","));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodDetail;
    }

    @SuppressLint("DefaultLocale")
    public JSONObject parsePddGoodDetail(JSONObject goodObj) {
        JSONObject goodDetail = new JSONObject();
        try {
            goodObj.getJSONObject("goods_detail_response");
            JSONObject detail = goodObj.getJSONObject("goods_detail_response").getJSONArray("goods_details").getJSONObject(0);
            goodDetail.put("title_img", detail.getJSONArray("goods_gallery_urls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodDetail;
    }
}
