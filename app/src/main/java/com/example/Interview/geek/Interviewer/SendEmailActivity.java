package com.example.Interview.geek.Interviewer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Interview.geek.R;
import com.example.Interview.geek.User.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class CONFIG {
    static String email = "email";
    static String password = "password";
}

class EmailSender extends AsyncTask<Void, Void, String>{

    String receiver;
    Context context;

    public EmailSender(String receiver, Context context){
        this.receiver = receiver;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        final String username = CONFIG.email;
        final String password = CONFIG.password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(CONFIG.email));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiver));
            message.setSubject("New response to your interview circular\n");
            message.setText("Dear User."
                    + "\n\n Someone from interview Geek responded to your interview circular!!! \n");
            Transport.send(message);
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    protected void onPostExecute(String response) {
        Toast.makeText(context, "Sent", Toast.LENGTH_LONG).show();

    }


}

public class SendEmailActivity extends AppCompatActivity {

    InterviewShoutout ist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        ist = new InterviewShoutout();
        ist = (InterviewShoutout) getIntent().getSerializableExtra("shout");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new EmailSender(ist.getEmail(), getApplicationContext()).execute();

        this.finish();
    }
}
