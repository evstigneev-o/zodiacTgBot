import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ZodiacTgBot extends TelegramLongPollingBot {
    public final static String BOT_NAME = {TG_BOT_NAME};

    public ZodiacTgBot(String botToken) {
        super(botToken);
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if (!update.hasMessage()) {
            return;
        }
        Message message = update.getMessage();
        if (!message.hasText()) {
            return;
        }
        String text = message.getText();
        if ("/start".equals(text)) {
            sendMessage(message.getChatId(), "Привет. Я помогу определить твой знак зодиака");
            sendMessage(message.getChatId(), "Введите дату рождения в формате DD.MM для определения знака зодиака");
            return;
        }
        if (text.matches("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])$")) {
            int day = Integer.parseInt(text.substring(0, text.indexOf('.')));
            int month = Integer.parseInt(text.substring(text.indexOf('.') + 1));
            sendMessage(message.getChatId(), "Ваш знак зодиака: " + ZodiacUtils.getZodiacName(day,month));
        } else {
            sendMessage(message.getChatId(), "Введенная дата \"" + text + "\" не соответствует формату DD.MM");
            sendMessage(message.getChatId(), "Введите дату в требуемом формате");
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
