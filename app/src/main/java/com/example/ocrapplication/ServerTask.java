package com.example.ocrapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerTask extends AsyncTask<Void, String, Void> {

    private String IP;
    private int port;
    private MainActivity main;
    private Socket socket;

    public ServerTask(String IP, int port, MainActivity main) {
        this.IP = IP;
        this.port = port;
        this.main = main;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            socket = new Socket(IP, port);
            //publishProgress("");
            Log.d("async","I got here. Socket is: " + socket.toString());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = buffer.readLine();
            while (line != null) {
                Log.d("async", "got a message: "+ line);
                publishProgress(line);
                line = buffer.readLine();
            }

        } catch (IOException e) {
            Log.d("async","an error accured: "+ e.toString());
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        main.useData(values[0]);
        if(main.getWriter() == null){
            try {
                main.setWriter(new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        main.writeConsole("Connection ended");
    }

    public Socket getSocket(){
        return this.socket;
    }

}
