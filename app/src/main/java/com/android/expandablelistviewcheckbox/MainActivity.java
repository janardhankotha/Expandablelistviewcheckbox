package com.android.expandablelistviewcheckbox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.expandablelistviewcheckbox.Model.DataItem;
import com.android.expandablelistviewcheckbox.Model.SubCategoryItem;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private ExpandableListView lvCategory;

    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;



    JSONParser jsonParser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CheckedActivity.class);
                startActivity(intent);
            }
        });
        lvCategory = findViewById(R.id.lvCategory);
        new userdata().execute();
       // setupReferences();





    }
    private void setupReferences() {

        lvCategory = findViewById(R.id.lvCategory);
        arCategory = new ArrayList<>();
        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Adventure");

        arSubCategory = new ArrayList<>();
        for(int i = 1; i < 6; i++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Adventure: "+i);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Art");
        arSubCategory = new ArrayList<>();
        for(int j = 1; j < 6; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Art: "+j);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Cooking");
        arSubCategory = new ArrayList<>();
        for(int k = 1; k < 6; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Cooking: "+k);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        Log.d("TAG", "setupReferences: "+arCategory.size());

        for(DataItem data : arCategory){
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList =new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID,data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME,data.getCategoryName());

            int countIsChecked = 0;
            for(SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID,subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME,subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID,subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED,subCategoryItem.getIsChecked());

                if(subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if(countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            }else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED,data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(this,parentItems,childItems,false);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }

    public class userdata extends AsyncTask<String, String, String> {

        String responce;
        String message;
        String headers;
        String childs;

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        protected String doInBackground(String... args) {
            Integer result = 0;
            List<NameValuePair> userpramas = new ArrayList<NameValuePair>();

            arCategory = new ArrayList<>();
            arSubCategory = new ArrayList<>();
            parentItems = new ArrayList<>();
            childItems = new ArrayList<>();

            Log.e("testing", "jsonParser startedkljhk");
            //userpramas.add(new BasicNameValuePair("feader_reg_id", id));
            //  Log.e("testing", "feader_reg_id" + id);

            JSONObject json = jsonParser.makeHttpRequest("http://g2evolution.in/pranitha/rest/getsub_cat", "GET", userpramas);


            Log.e("testing1", "jsonParser" + json);


            if (json == null) {

                Log.e("testing1", "jon11111111111111111");
                // Toast.makeText(getActivity(),"Data is not Found",Toast.LENGTH_LONG);

                return responce;
            } else {
                Log.e("testing1", "jon2222222222222");
                // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {

                    JSONObject response = new JSONObject(json.toString());

                    Log.e("testing1", "jsonParser2222" + json);

                    //JSONObject jsonArray1 = new JSONObject(json.toString());
                    // Result = response.getString("status");
                    JSONArray posts = response.optJSONArray("data");
                    Log.e("testing1", "jsonParser3333" + posts);

                    for (int i = 0; i < posts.length(); i++) {

                        JSONObject post = posts.optJSONObject(i);

                        DataItem dataItem = new DataItem();
                        dataItem.setCategoryId(post.getString("catId"));
                        dataItem.setCategoryName(post.getString("categoryName"));

                        arSubCategory = new ArrayList<>();

                        JSONArray posts2 = post.optJSONArray("details");

                        for (int i1 = 0; i1 < posts2.length(); i1++) {
                            JSONObject post2 = posts2.optJSONObject(i1);

                            SubCategoryItem subCategoryItem = new SubCategoryItem();
                            subCategoryItem.setCategoryId(String.valueOf(i));
                            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                            dataItem.setCategoryId(post2.getString("catId"));
                            subCategoryItem.setSubId(post2.getString("subcatId"));
                            subCategoryItem.setSubCategoryName(post2.getString("subcatname"));
                            arSubCategory.add(subCategoryItem);

                        }
                        dataItem.setSubCategory(arSubCategory);
                        arCategory.add(dataItem);
                    }

                    for(DataItem data : arCategory) {
//                        Log.i("Item id",item.id);
                        ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> mapParent = new HashMap<String, String>();

                        mapParent.put(ConstantManager.Parameter.CATEGORY_ID, data.getCategoryId());
                        mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());

                        int countIsChecked = 0;
                        for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                            HashMap<String, String> mapChild = new HashMap<String, String>();
                            mapChild.put(ConstantManager.Parameter.SUB_ID, subCategoryItem.getSubId());
                            mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                            mapChild.put(ConstantManager.Parameter.CATEGORY_ID, subCategoryItem.getCategoryId());
                            mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());

                            if (subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                                countIsChecked++;
                            }
                            childArrayList.add(mapChild);
                        }

                        if (countIsChecked == data.getSubCategory().size()) {

                            data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
                        } else {
                            data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                        }

                        mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsChecked());
                        childItems.add(childArrayList);
                        parentItems.add(mapParent);
                    }
                    ConstantManager.parentItems = parentItems;
                    ConstantManager.childItems = childItems;



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return responce;
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pDialog.dismiss();
            Log.e("testing", "result is === " + result);

            myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(MainActivity.this,parentItems,childItems,false);
            lvCategory.setAdapter(myCategoriesExpandableListAdapter);
        }

    }

}