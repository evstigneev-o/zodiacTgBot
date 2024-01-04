import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
        if (update.hasCallbackQuery()) {
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
            sendKeyboard(message.getChatId(), "Выбери месяц твоего рождения:", selectMonthKeyboard());
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        System.out.println(callbackQuery.getData());
        try {
            AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (callbackQuery.getData() == null) {
            return;
        }

        if (callbackQuery.getData().equals("again")) {
            sendKeyboard(callbackQuery.getMessage().getChatId(), "Выбери месяц твоего рождения:", selectMonthKeyboard());
            deleteMessage(callbackQuery);
            return;
        }

        Month selectedMonth = null;
        try {
            selectedMonth = Month.valueOf(callbackQuery.getData());
        } catch (Exception ignored) {
        }

        if (selectedMonth != null) {
            sendMessage(callbackQuery.getMessage().getChatId(), "Вы выбрали месяц: " + getRusMonthName(selectedMonth));

            try {
                execute(DeleteMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .messageId(callbackQuery.getMessage().getMessageId())
                        .build());
            } catch (Exception ignored) {

            }

            sendKeyboard(callbackQuery.getMessage().getChatId(), "Выберите день рождения: ", selectDayOfMonthKeyboard(selectedMonth));
        }
        if (callbackQuery.getData().matches("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])$")) {
            int day = Integer.parseInt(callbackQuery.getData().substring(0, callbackQuery.getData().indexOf('.')));
            int month = Integer.parseInt(callbackQuery.getData().substring(callbackQuery.getData().indexOf('.') + 1));
            deleteMessage(callbackQuery);
            sendMessage(callbackQuery.getMessage().getChatId(), "Вы выбрали день: " + day);
            sendMessage(callbackQuery.getMessage().getChatId(), "Ваш знак зодиака: " + ZodiacUtils.getZodiacName(day, month));
            sendKeyboard(callbackQuery.getMessage().getChatId(), "Попробовать еще?", tryAgainKeyboard());
        }


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

    private static ReplyKeyboard selectDayOfMonthKeyboard(Month month) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>(6);
        for (int i = 0; i < 7; i++) {
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>(5);
            for (int j = 0; j < 5; j++) {
                int day = i + j * 7 + 1;

                String buttonText = day <= month.maxLength() ? String.valueOf(day) : "x";
                String buttonData = day <= month.maxLength() ? String.format("%02d.%02d", day, month.getValue()) : "x";
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(buttonText)
                        .callbackData(buttonData)
                        .build();
                keyboardButtons.add(button);
            }
            keyboardRows.add(keyboardButtons);
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    private static ReplyKeyboard tryAgainKeyboard(){
    return InlineKeyboardMarkup.builder()
            .keyboard(List.of(List.of(InlineKeyboardButton.builder()
                    .text("Попробовать еще раз")
                    .callbackData("again")
                    .build())))
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

    private void deleteMessage(CallbackQuery callbackQuery) {
        try {
            execute(DeleteMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .build());
        } catch (Exception ignored) {
        }
    }

    private static String getRusMonthName(Month month) {
        return StringUtils.capitalize(month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru")));
    }
}
