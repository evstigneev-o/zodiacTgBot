# Знаки Зодиака Телеграм-Бот

Этот телеграм-бот предоставляет информацию о знаках зодиака на основе введенной даты.

## Как использовать бота

1. **Запуск бота:**
    - Зарегистрируйте бота с помощью BotFather в Telegram.
    - Замените значение `BOT_TOKEN` `BOT_NAME` вашим токеном и именем бота.
    - Запустите Main

```java
public class Main {
    public static void main(String[] args) {
        String botToken = {BOT_TOKEN};
        String botName = {BOT_NAME};
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new ZodiacTgBot(botToken, botName));
    }
}
```
2. **Взаимодействие с ботом:**
   - Начните чат с ботом, отправив /start.
   - Введите дату в формате DD.MM (например, 25.03).
   - Бот вернет ваш знак зодиака на основе введенной даты.
## Структура проекта
- ZodiacTgBot: Основной класс бота, который обрабатывает входящие обновления.
- ZodiacUtils: Утилитарный класс для определения знака зодиака по введенной дате.
## Зависимости

Бот написан на языке Java с использованием библиотеки [TelegramBots](https://github.com/rubenlagus/TelegramBots).

```groovy
implementation 'org.telegram:telegrambots:6.8.0'
```
