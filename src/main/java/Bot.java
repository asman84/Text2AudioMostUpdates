import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import service.Button;
import service.Text2Speech;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Bot extends TelegramLongPollingBot {
    Button button = new Button();
    String fileName;
    String commands = "others";
    Text2Speech convertor = new Text2Speech();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        System.out.println(text);
        sendMsg(message, "bu Text2SpeechMost Updated @djamalov3_bot ni javobi");
    }

    private void sendAudio(Message message, String text, String s) {
        try {
            convertor.go(text, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendAudio audio = new SendAudio();
        audio.setChatId(message.getChatId().toString());
        audio.setReplyToMessageId(message.getMessageId());
        audio.setNewAudio(new File("D:\\" + fileName + ".mp3"));
        try {
            sendAudio(audio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(s);
        if (message.getText().equals("/start") || message.getText().equals("◀️Orqaga")) {
            this.commands = "others";
            button.setButtonsMain(sendMessage);
        }
        if (message.getText().equals("Kanaldagi yangiliklarni audio kurinishida eshitish \uD83D\uDCDA")) {
            this.commands = "others";
            button.setButtonsChannel(sendMessage);
        }
        if (message.getText().equals("Sozlash")) {
            this.commands = "others";
            sendMessage.setText("Kim uqib berishini xoxlaysiz? Ayol jurnalistmi yoki erkak?");
            button.setButtonReader(sendMessage);
        }
        if (message.getText().equals("\uD83D\uDC71Erkak")) {
            this.commands = "others";
            sendMessage.setText("erkak jurnalist textni o'qib beradi");
            convertor.setReader("zahar");
        }
        if (message.getText().equals("\uD83D\uDC69\uD83C\uDFFBAyol")) {
            this.commands = "others";
            sendMessage.setText("ayol jurnalist textni o'qib beradi");
            convertor.setReader("oksana");

        }


        if (message.getText().equals("Text2Audio")) {
            this.commands = "audio";
            sendMessage.setText("Audio kurinishida eshitmoqchi bulgan textingizni yuboring");
            button.setButtonBack(sendMessage);
        }
        if (this.commands.equals("audio") && !message.getText().equals("Text2Audio")) {
            String audioText = message.getText();
            String[] textSplitted = audioText.split(" ");
            this.fileName = textSplitted[0];
            try {
                audioText = URLEncoder.encode(audioText, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sendAudio(message, audioText, "sizni tekstingiz audiosi: ");
            button.setButtonBack(sendMessage);
        }
        if (message.getText().equals("Ruyxatdan utish  \uD83D\uDEE0")) {
            this.commands = "others";
            button.setButtonBack(sendMessage);
            sendMessage.setText("Iltimos, Ism, Familiya, SHarifingizni kiriting. Masalan:\"Ivan Ivonov Ivanovich\".");
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "@djamalov3_bot";
    }

    public String getBotToken() {
        return "598129685:AAHzIkUy3Lfzp3K2COnV_NlIUUVSlft9ygI";
    }
}
