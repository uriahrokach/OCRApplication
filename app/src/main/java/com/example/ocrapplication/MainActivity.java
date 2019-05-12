package com.example.ocrapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    public static String IP = "79.182.25.149";
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

        this.image = ((BitmapDrawable) this.textImage.getDrawable()).getBitmap();
        selectHandler = new SelectButtonHandler(this, selectBtn, textImage, console);

        task = new ServerTask(IP, PORT, this);
        task.execute();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writer.println(selectHandler.getImageUri());
                        writer.flush();
                    }
                });
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
        Intent intent = new Intent(this, null);
        intent.putExtra(EXTRA_TEXT, value);
        startActivity(intent);
    }

    public void writeConsole(String connection_ended) {
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
}
