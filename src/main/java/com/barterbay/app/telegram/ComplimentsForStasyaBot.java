package com.barterbay.app.telegram;

import com.barterbay.app.domain.dto.telegram.HoroscopeDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.barterbay.app.telegram.ComplimentBot.createSendMessageForTelegram;
import static com.barterbay.app.util.CollectionsUtil.getRandomSetElement;
import static com.barterbay.app.util.Constants.START_COMMAND;

@Service
@Profile("prod")
public class ComplimentsForStasyaBot extends TelegramLongPollingBot {

  private static final Set<String> DAY_COMMAND_SET = Set.of("yesterday", "tomorrow", "today");
  private static final String SAY_SOMETHING_COMMAND = "/saysomething";
  private static final String HOROSCOPE_COMMAND = "/horoscope";
  private static final Map<Integer, String> HOROSCOPE_MAP = new HashMap<>();

  private String horoscopeSign;

  private Set<String> phrases;

  @Value("${compliment-stasya-telegram-bot.name}")
  private String botUsername;

  @Value("${compliment-stasya-telegram-bot.token}")
  private String botToken;

  @Value("${horoscope-api.url}")
  private String horoscopeAPIUrl;

  private final WebClient webClient;

  public ComplimentsForStasyaBot(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  @SneakyThrows
  public void onUpdateReceived(Update update) {
    final var botCommand = update.getMessage().getText();

    if (START_COMMAND.equals(botCommand)) {
      sendGreeting(update);
    } else if (SAY_SOMETHING_COMMAND.equals(botCommand)) {
      execute(createPrediction(update));
    } else if (HOROSCOPE_COMMAND.equals(botCommand)) {
      sendChooseZodiacSign(update);
    } else if (Character.isDigit(botCommand.charAt(0))) {
      sendChooseDayForHoroscope(update);
    } else if (DAY_COMMAND_SET.contains(botCommand)) {
      sendHoroscope(update);
    }
  }

  @SneakyThrows
  private void sendHoroscope(Update update) {
    final var dayForHoroscope = update.getMessage().getText();
    final var fullUrl = horoscopeAPIUrl
      .concat("/?sign=")
      .concat(horoscopeSign)
      .concat("&day=")
      .concat(dayForHoroscope);

    var horoscope = webClient
      .post()
      .uri(fullUrl)
      .retrieve()
      .bodyToMono(HoroscopeDTO.class)
      .map(HoroscopeDTO::toString)
      .block();

    final var sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    if (horoscope == null) {
      horoscope = "Ooppss, something went wrong. Please, contact administrator";
    }
    sendMessage.setText(horoscope);

    execute(sendMessage);
  }

  @SneakyThrows
  private void sendChooseDayForHoroscope(Update update) {
    final var selectedZodiacSign = Integer.parseInt(update.getMessage().getText());
    final var zodiacSign = HOROSCOPE_MAP.get(selectedZodiacSign);
    horoscopeSign = zodiacSign.split(" ")[0].toLowerCase();

    var additionalInfo = "";

    if ("aries".equals(horoscopeSign)) {
      additionalInfo = ".\n".concat("p.s. Aries - this is my favorite zodiac sign despite being programmed by a guy whose zodiac sign is cancer :DD");
    }

    final var sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText("What a beautiful choice - "
      .concat(zodiacSign)
      .concat(" . Now you need to select for which day you`d like to get horoscope")
      .concat(additionalInfo));

    final var replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setInputFieldPlaceholder("Type or day a select it");
    replyKeyboardMarkup.setSelective(false);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    final var keyboardButtons = new KeyboardRow();
    keyboardButtons.add("today");
    keyboardButtons.add("tomorrow");
    keyboardButtons.add("yesterday");

    replyKeyboardMarkup.setKeyboard(List.of(keyboardButtons));

    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    execute(sendMessage);
  }

  private void sendChooseZodiacSign(Update update) throws org.telegram.telegrambots.meta.exceptions.TelegramApiException {
    final var sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText("Stasya, please, choose the zodiac sign for which you want to see the horoscope\n" +
      "1) Aries - Овен\n" +
      "2) Cancer - Рак\n" +
      "3) Taurus - Телец\n" +
      "4) Gemini - Близнецы\n" +
      "5) Leo - Лев\n" +
      "6) Virgo - Дева\n" +
      "7) Libra - Весы\n" +
      "8) Scorpio - Скорпион\n" +
      "9) Sagittarius - Стрелец\n" +
      "10) Capricorn - Козерог\n" +
      "11) Pisces - Рыбы\n" +
      "12) Aquarius - Водолей");

    final var replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setInputFieldPlaceholder("Type or select a number");
    replyKeyboardMarkup.setSelective(false);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    final var keyboardButtons = new KeyboardRow();
    keyboardButtons.add("1");
    keyboardButtons.add("2");
    keyboardButtons.add("3");
    keyboardButtons.add("4");
    keyboardButtons.add("5");
    keyboardButtons.add("6");
    keyboardButtons.add("7");
    keyboardButtons.add("8");
    keyboardButtons.add("9");
    keyboardButtons.add("10");
    keyboardButtons.add("11");
    keyboardButtons.add("12");

    replyKeyboardMarkup.setKeyboard(List.of(keyboardButtons));

    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    execute(sendMessage);
  }

  private void sendGreeting(Update update) throws org.telegram.telegrambots.meta.exceptions.TelegramApiException {
    final var userFirstName = update.getMessage().getFrom().getFirstName();
    final var messageText = "Bonjour Mademoiselle "
      .concat(userFirstName)
      .concat(". Finally you are here, glad to see you. ")
      .concat("Hurry up, press /saysomething or /horoscope if you want to hear some magic");
    execute(createSendMessageForTelegram(update, messageText));
  }

  private SendMessage createPrediction(Update update) {
    final var userFirstName = update.getMessage().getFrom().getFirstName();
    final var randomPrediction = getRandomSetElement(phrases);
    final var messageText = userFirstName
      .concat(", ")
      .concat(randomPrediction);
    return createSendMessageForTelegram(update, messageText);
  }

  @EventListener(ContextRefreshedEvent.class)
  public void fillPhrasesSet() {
    HOROSCOPE_MAP.put(1, "Aries - Овен");
    HOROSCOPE_MAP.put(2, "Cancer - Рак");
    HOROSCOPE_MAP.put(3, "Taurus - Телец");
    HOROSCOPE_MAP.put(4, "Gemini - Близнецы");
    HOROSCOPE_MAP.put(5, "Leo - Лев");
    HOROSCOPE_MAP.put(6, "Virgo - Дева");
    HOROSCOPE_MAP.put(7, "Libra - Весы");
    HOROSCOPE_MAP.put(8, "Scorpio - Скорпион");
    HOROSCOPE_MAP.put(9, "Sagittarius - Стрелец");
    HOROSCOPE_MAP.put(10, "Capricorn - Козерог");
    HOROSCOPE_MAP.put(11, "Pisces - Рыбы");
    HOROSCOPE_MAP.put(12, "Aquarius - Водолей");

    phrases = new HashSet<>();
    phrases.add("I am really impressed with how well you listen to everything that everyone says. It is obvious that you listen to everyone before you make a decision on what to do next.");
    phrases.add("Все же, вискикурня – это не бар с виски для кур :D");
    phrases.add("It's a well-known fact that bad sleeping schedule has a negative effect on beauty, but for some reason it doesn't work for you - maybe you're a witch? :D");
    phrases.add("Sex cruise - maybe this is what you need now? :D");
    phrases.add("Sphinxes are not your breed of cat :D");
    phrases.add("Певица в полиции - звучит хорошо)");
    phrases.add("Tip of the day - give Lost a chance, have free time - watch 2-3 episodes - you won't regret about it");
    phrases.add("Recruiter team lead position is close - keep  going");
    phrases.add("Riding a bike and talking on the iPhone at the same time is not a good idea :D");
    phrases.add("When you are offered to play the game - it will not necessarily be a Dota :D");
    phrases.add("Walking with an iPhone in a severe thunderstorm is dangerous, what if it gets wet? :D");
    phrases.add("It's coffee time, isn't it? Latte based on coconut milk - sounds delicious ^^");
    phrases.add("The best recruiter with whom I talked");
    phrases.add("You're doing great interviews, asking questions about hobby, in order to relax a person and open up - this is a very professional movement");
    phrases.add("Ciklum faces crisis as it loses top recruiter");
    phrases.add("I'm really impressed with your taste and style. It is really obvious that you have thought carefully about how to decorate the inside of this room.");
    phrases.add("I never realized just how great your sense of humor was! You always know just what to say to make someone else laugh. That is a great way to maintain relationships throughout your life. ");
    phrases.add("I consider myself lucky to have gotten to know you. You have had a major impact on my life, and I truly appreciate it.");
  }
}
