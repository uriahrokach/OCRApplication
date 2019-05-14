package com.example.ocrapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    public static String IP = "10.0.0.17";
    public static int PORT = 8080;
    public static String EXTRA_TEXT = "com.example.ocrapplication.EXTRA_TEXT";

    public Button selectBtn;
    public Button submitBtn;
    public TextView console;
    public ImageView textImage;

    private SelectButtonHandler selectHandler;
    private PrintWriter writer;
    private Bitmap image;

    private String message;
    private ServerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.selectBtn = findViewById(UILayout.BUTTONS.SELECT);
        this.submitBtn = findViewById(UILayout.BUTTONS.SUBMIT);
        this.console = findViewById(UILayout.TEXTS.CONSOLE);
        this.textImage = findViewById(UILayout.IMAGES.TEXT_IMAGE);

        //Log.d("MainActivity", "this works?");

        this.image = ((BitmapDrawable) this.textImage.getDrawable()).getBitmap();
        selectHandler = new SelectButtonHandler(this, selectBtn, textImage, console);

        task = new ServerTask(IP, PORT, this);
        task.execute();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBtn.setActivated(false);
                submitBtn.setActivated(false);
                writeConsole("sending message...");

                if(writer == null){
                    try {
                        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(task.getSocket().getOutputStream())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writer.println(selectHandler.getImageUri());
                        writer.flush();

                    }
                });
                t.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SelectButtonHandler.PICK_IMAGE_CODE && resultCode == RESULT_OK)
            selectHandler.changeImage(data);
    }

    public void useData(String value) {
        selectBtn.setActivated(true);
        submitBtn.setActivated(true);
        Intent intent = new Intent(this, AnswerActivity.class);
        intent.putExtra(EXTRA_TEXT, value);
        startActivity(intent);
    }

    public void writeConsole(String message) {
        this.console.setText(message);
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
}
