import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ZodiacTgBot extends TelegramLongPollingBot {
    private final String botName;

    public ZodiacTgBot(String botToken, String botName) {
        super(botToken);
        this.botName = botName;
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if (!update.hasMessage() && !update.hasCallbackQuery()) {
            return;
        }
        if(update.hasCallbackQuery()){
            processCallbackQuery(update.getCallbackQuery());
            return;
        }
        Message message = update.getMessage();
        if (!message.hasText()) {
            return;
        }
        String text = message.getText();
        if ("/start".equals(text)) {
            sendMessage(message.getChatId(), "Привет. Я помогу определить твой знак зодиака");
            sendKeyboard(message.getChatId(), "Выбери месяц твоего рождения:" ,selectMonthKeyboard());
            return;
        }
        if (text.matches("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])$")) {
            int day = Integer.parseInt(text.substring(0, text.indexOf('.')));
            int month = Integer.parseInt(text.substring(text.indexOf('.') + 1));
            sendMessage(message.getChatId(), "Ваш знак зодиака: " + ZodiacUtils.getZodiacName(day, month));
        } else {
            sendMessage(message.getChatId(), "Введенная дата \"" + text + "\" не соответствует формату DD.MM");
            sendMessage(message.getChatId(), "Введите дату в требуемом формате");
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        System.out.println(callbackQuery.getData());
        try{
            AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .build();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        Month selectedMonth = Month.valueOf(callbackQuery.getData());
        sendMessage(callbackQuery.getMessage().getChatId(), "Вы выбрали месяц: " + getRusMonthName(selectedMonth));
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private static ReplyKeyboard selectMonthKeyboard() {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>(6);
        Month[] months = Month.values();
        for (int i = 0; i < months.length / 2; i++) {
            Month month1 = months[i];
            Month month2 = months[i + 6];
            InlineKeyboardButton keyboardButton1 = InlineKeyboardButton.builder()
                    .text(getRusMonthName(month1))
                    .callbackData(month1.name())
                    .build();
            InlineKeyboardButton keyboardButton2 = InlineKeyboardButton.builder()
                    .text(getRusMonthName(month2))
                    .callbackData(month2.name())
                    .build();
            keyboardRows.add(List.of(keyboardButton1, keyboardButton2));
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
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

    private void sendKeyboard(Long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage message = SendMessage.builder()
                .text(text)
                .replyMarkup(keyboard)
                .chatId(chatId)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getRusMonthName(Month month){
        return StringUtils.capitalize(month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru")));
    }
}
