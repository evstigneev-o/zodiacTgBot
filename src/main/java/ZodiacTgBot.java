import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ZodiacTgBot extends TelegramLongPollingBot {
    public final static String BOT_NAME = "mrZodiac_bot";

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
            SendMessage sendMessage = SendMessage.builder()
                    .text("Введите дату в формате DD.MM для определения знака зодиака")
                    .chatId(message.getChatId())
                    .build();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (text.matches("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])$")) {
            int day = Integer.parseInt(text.substring(0, text.indexOf('.')));
            int month = Integer.parseInt(text.substring(text.indexOf('.') + 1));
            SendMessage sendMessage = SendMessage.builder()
                    .text("Ваш знак зодиака: " + ZodiacUtils.getZodiacName(day,month))
                    .chatId(message.getChatId())
                    .build();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            SendMessage sendMessage = SendMessage.builder()
                    .text("Введенная дата \"" + text + "\" не соответствует формату DD.MM Введите дату в требуемом формате")
                    .chatId(message.getChatId())
                    .build();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
}
