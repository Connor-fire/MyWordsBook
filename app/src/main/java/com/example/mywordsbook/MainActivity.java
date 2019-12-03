package com.example.mywordsbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Word> wordList=new ArrayList<>();
    private MyDatabaseHelper dbHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("帮助：");
                dialog.setMessage("单击单词进行查询"+"\n"+"长按单词删除"+"\n"+"横屏也可使用");
                dialog.setPositiveButton("确定",null).show();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper =new MyDatabaseHelper(this,"WordBook.db",null ,2);

//        try{
//
//            Button cre_btn=(Button) findViewById(R.id.cre_btn);
//            cre_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dbHelper.getWritableDatabase();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        initWords();
        final WordAdapter adapter=new WordAdapter(MainActivity.this,R.layout.word_item,wordList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word=wordList.get(i);
                try{
                    RightFragment rightFragment=(RightFragment) getSupportFragmentManager().findFragmentById(R.id.right_fragment);
                    rightFragment.refresh(word.CHN,word.SEN);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this,word.getCHN(),Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("单词释义：");
                    dialog.setMessage("中文："+"\n"+word.getCHN()+"\n"+"例句："+"\n"+word.getSEN());
                    dialog.setPositiveButton("确定",null).show();
                }
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Word word=wordList.get(i);

                AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("删除提醒！");
                        dialog.setMessage("确定要删除所选单词？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String ENG = word.getENG();
                        String CHN = word.getCHN();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("Word","ENG = ? AND CHN = ?",new String[]{ENG,CHN});
                        Toast.makeText(MainActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                        try{
                            wordList.clear();
                            SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                            //Cursor cursor = db.rawQuery("Select * from Word", null);
                            String[] coiumns={"ENG","CHN","SEN"};
                            Cursor cursor =db1.query("Word",coiumns,null,null,null,null,null);
                            if(cursor.moveToFirst()){
                                do{
                                    wordList.add(new Word(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                                }while(cursor.moveToNext());
                            }
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ListView listView = (ListView) findViewById(R.id.list_view);
                        listView.setAdapter(adapter);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
                return true;
            }
        });


        try{
            Button new_btn=(Button) findViewById(R.id.new_btn);
            new_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText  edit_eng_txt=(EditText) findViewById(R.id.edit_eng_txt) ;
                    EditText  edit_chn_txt=(EditText) findViewById(R.id.edit_chn_txt) ;
                    EditText  edit_sen_txt=(EditText) findViewById(R.id.edit_sen_txt) ;
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    try{
                        values.put("ENG",edit_eng_txt.getText().toString());
                        values.put("CHN",edit_chn_txt.getText().toString());
                        values.put("SEN",edit_sen_txt.getText().toString());
                        db.insert("Word",null,values);
                        Toast.makeText(MainActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                    }

                    try{
                        wordList.clear();
                        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                        //Cursor cursor = db.rawQuery("Select * from Word", null);
                        String[] coiumns={"ENG","CHN","SEN"};
                        Cursor cursor =db1.query("Word",coiumns,null,null,null,null,null);
                        if(cursor.moveToFirst()){
                            do{
                                wordList.add(new Word(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                            }while(cursor.moveToNext());
                        }
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ListView listView = (ListView) findViewById(R.id.list_view);
                    listView.setAdapter(adapter);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        try{
            Button select_chn_btn=(Button) findViewById(R.id.select_chn_btn);
            select_chn_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText  edit_chn_txt=(EditText) findViewById(R.id.edit_chn_txt) ;
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    try{
                        Cursor cursor=db.rawQuery("Select * from Word where CHN = ?", new String[]{edit_chn_txt.getText().toString()});
                        cursor.moveToFirst();
                        String ENG=cursor.getString(cursor.getColumnIndex("ENG"));
                        String SEN = cursor.getString(cursor.getColumnIndex("SEN"));
                        //Toast.makeText(MainActivity.this,ENG,Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("单词释义：");
                        dialog.setMessage("英文："+"\n"+ENG+"\n"+"例句："+"\n"+SEN);
                        dialog.setPositiveButton("确定",null).show();

                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"查无此词",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"已切换至横屏",Toast.LENGTH_SHORT).show();
        }

        try{
            Button select_eng_btn=(Button) findViewById(R.id.select_eng_btn);
            select_eng_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText  edit_eng_txt=(EditText) findViewById(R.id.edit_eng_txt) ;
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    try{
                        //Cursor cursor =db.query("Word",null,"where CHN ="+edit_chn_txt.getText().toString(),null,null,null,null);
                        Cursor cursor=db.rawQuery("Select * from Word where ENG = ?", new String[]{edit_eng_txt.getText().toString()});
                        cursor.moveToFirst();
                        String CHN=cursor.getString(cursor.getColumnIndex("CHN"));
                        String SEN=cursor.getString(cursor.getColumnIndex("SEN"));
                        //Toast.makeText(MainActivity.this,CHN,Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("单词释义：");
                        dialog.setMessage("中文："+"\n"+CHN+"\n"+"例句："+"\n"+SEN);
                        dialog.setPositiveButton("确定",null).show();

                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"查无此词",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            Button fuzzy_que_btn=(Button) findViewById(R.id.fuzzy_que_btn);
            fuzzy_que_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText  edit_eng_txt=(EditText) findViewById(R.id.edit_eng_txt) ;
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    try{
                        //Cursor cursor =db.query("Word",null,"where CHN ="+edit_chn_txt.getText().toString(),null,null,null,null);
                        String str ="%"+edit_eng_txt.getText().toString()+"%";
                        Cursor cursor=db.rawQuery("Select * from Word where ENG like ?", new String[]{str});
                        String ENGs=" ";
                        String CHNs=" ";
                        if(cursor.moveToFirst()){
                            do{
                                String ENG=cursor.getString(cursor.getColumnIndex("ENG"));
                                String CHN=cursor.getString(cursor.getColumnIndex("CHN"));
                                ENGs=ENGs+ENG+"  ";
                                CHNs=CHNs+CHN+"  ";
                            }while(cursor.moveToNext());
                        }
                        //Toast.makeText(MainActivity.this,ENGs+"\n"+CHNs,Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("单词释义：");
                        dialog.setMessage("相关英文："+ENGs+"\n"+"相关中文："+CHNs);
                        dialog.setPositiveButton("确定",null).show();

                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"查无此词",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            Button edit_word_button=(Button) findViewById(R.id.edit_word_button);
            edit_word_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText  edit_eng_txt=(EditText) findViewById(R.id.edit_eng_txt) ;
                    EditText  edit_chn_txt=(EditText) findViewById(R.id.edit_chn_txt) ;
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    try{

                        ContentValues values=new ContentValues();
                        values.put("CHN",edit_chn_txt.getText().toString());
                        db.update("Word",values,"ENG = ?",new String[]{edit_eng_txt.getText().toString()});

                        try{
                            wordList.clear();
                            SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                            //Cursor cursor = db.rawQuery("Select * from Word", null);
                            String[] coiumns={"ENG","CHN","SEN"};
                            Cursor cursor =db1.query("Word",coiumns,null,null,null,null,null);
                            if(cursor.moveToFirst()){
                                do{
                                    wordList.add(new Word(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                                }while(cursor.moveToNext());
                            }
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void initWords(){
//        Word A= new Word("ABC","AB");
//        Word B= new Word("ABC","ABC");
//        Word C= new Word("ABC","ABC");
//        Word D= new Word("ABC","ABC");
//        Word E= new Word("ABC","ABC");
//        Word F= new Word("ABC","ABC");
//        Word G= new Word("ABC","ABC");
//        Word H= new Word("ABC","ABC");
//        Word I= new Word("ABC","ABC");
//        Word J= new Word("ABC","ABC");
//        Word K= new Word("ABC","ABC");
//        Word L= new Word("123","ABC");
//        wordList.add(A);
//        wordList.add(B);
//        wordList.add(C);
//        wordList.add(D);
//        wordList.add(E);
//        wordList.add(F);
//        wordList.add(G);
//        wordList.add(H);
//        wordList.add(I);
//        wordList.add(J);
//        wordList.add(K);
//        wordList.add(L);
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //Cursor cursor = db.rawQuery("Select * from Word", null);
            String[] coiumns={"ENG","CHN","SEN"};
           Cursor cursor =db.query("Word",coiumns,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    wordList.add(new Word(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                }while(cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
