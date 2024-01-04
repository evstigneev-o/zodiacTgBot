# Знаки Зодиака телеграм-бот

Этот телеграм-бот предоставляет информацию о знаках зодиака на основе введенной даты.

## Как использовать бота

1. **Запуск бота:**
    - Зарегистрируйте бота с помощью BotFather в Telegram.
    - Замените значение `BOT_TOKEN` `BOT_NAME` вашим токеном и именем бота.
    - Запустите main

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
   - Выберите месяц рождения на встроенной клавиатуре
   - Выберите день рождения на встроенной клавиатуре
   - Бот вернет ваш знак зодиака на основе введенной даты.
   - Для перезапуска нажмите кнопку "Попробовать еще раз"
## Структура проекта
- ZodiacTgBot: Основной класс бота, который обрабатывает входящие обновления.
- ZodiacUtils: Утилитарный класс для определения знака зодиака по введенной дате.
## Зависимости

Бот написан на языке Java 17 с использованием библиотеки [TelegramBots](https://github.com/rubenlagus/TelegramBots).

```groovy
implementation 'org.telegram:telegrambots:6.8.0'
```
