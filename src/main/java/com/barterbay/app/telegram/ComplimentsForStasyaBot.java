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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.barterbay.app.telegram.ComplimentBot.createSendMessageForTelegram;
import static com.barterbay.app.util.CollectionsUtil.getRandomSetElement;
import static com.barterbay.app.util.Constants.START_COMMAND;

@Service
@Profile("prod")
public class ComplimentsForStasyaBot extends TelegramLongPollingBot {

  private static final Set<String> DAY_COMMAND_SET = Set.of("yesterday", "tomorrow", "today");
  private static final String SAY_SOMETHING_COMMAND = "/saysomething";
  private static final String FORTUNE = "/fortune";
  private static final String ASK_CRYSTAL_BALL = "/askcrystalball";
  private static final String HOROSCOPE_COMMAND = "/horoscope";
  private static final Map<Integer, String> HOROSCOPE_MAP = new HashMap<>();

  private String horoscopeSign;

  private Set<String> phrases;
  private Set<String> fortune;

  @Value("${compliment-stasya-telegram-bot.name}")
  private String botUsername;

  @Value("${compliment-stasya-telegram-bot.token}")
  private String botToken;

  @Value("${horoscope-api.url}")
  private String horoscopeAPIUrl;

  @Value("${yes-or-no-api.url}")
  private String yesOrNoAPIUrl;

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
      execute(saySomething(update, phrases));
    } else if (HOROSCOPE_COMMAND.equals(botCommand)) {
      sendChooseZodiacSign(update);
    } else if (Character.isDigit(botCommand.charAt(0))) {
      sendChooseDayForHoroscope(update);
    } else if (DAY_COMMAND_SET.contains(botCommand)) {
      sendHoroscope(update);
    } else if (FORTUNE.equals(botCommand)) {
      execute(saySomething(update, fortune));
    } else if (ASK_CRYSTAL_BALL.equals(botCommand)) {
      askToProvideQuestion(update);
    } else {
      responseForCrystalBall(update);
    }
  }

  @SneakyThrows
  private void responseForCrystalBall(Update update) {
    final var randomAnswers = Arrays.stream(webClient
      .get()
      .uri(yesOrNoAPIUrl)
      .retrieve()
      .bodyToMono(String.class)
      .block()
      .split("\n"))
      .collect(Collectors.toSet());
    final var randomAnswer = getRandomSetElement(randomAnswers);

    final var sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText(randomAnswer);

    execute(sendMessage);
  }

  @SneakyThrows
  private void askToProvideQuestion(Update update) {
    final var sendMessage = new SendMessage();
    final var userFirstName = update.getMessage().getFrom().getFirstName();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText(userFirstName.concat(", please, enter any Yes or No question"));

    execute(sendMessage);
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
      additionalInfo = ".\n\n".concat("p.s. Aries - this is my favorite zodiac sign despite being programmed by a guy whose zodiac sign is cancer :DD");
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
      .concat("Hurry up, press /saysomething - if you want to hear something funny\n" +
        "Press /horoscope - if you want to hear some magic\n" +
        "Press /fortune to generate fortune cookie sayings\n" +
        "Press /askcrystalball - if you have a question for magic crystal ball");
    execute(createSendMessageForTelegram(update, messageText));
  }

  private SendMessage saySomething(Update update, Set<String> set) {
    final var userFirstName = update.getMessage().getFrom().getFirstName();
    final var randomPrediction = getRandomSetElement(set);
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
    phrases.add("У тебя нежно-обнимательный взгляд!");
    phrases.add("Твои глаза – они очаровывают)");
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
    phrases.add("Свидание с бабушкой француженкой? Звучит как идеальное воскресенье :D");
    phrases.add("Ты очень внимательна, замечаешь мелкие детали");
    phrases.add("Ты отличный слушатель, с тобой приятно вести диалог");
    phrases.add("Будь внимательней с тем - кто заставляет испытывать тебя эмоциональные качели :D");
    phrases.add("Совет дня - не стоит смотерть Hypnotic фильм, он не очень :D");
    phrases.add("Говорят, глаза - это зеркало души. Это многое объясняет)");
    phrases.add("Have you ever noticed how green eyes look absolutely the same as gray eyes? :D");
    phrases.add("Ти пахнеш, наче квіти \nІ сяєш наче сонце\nРаді твоєй красоти\nВиплигнув би я в оконце :D");
    phrases.add("Juste une phrase aléatoire en français pour que ce bot commence à écrire des phrases en trois langues :D");

    fortune = new HashSet<>();
    fortune.add("People are naturally attracted to you.");
    fortune.add("You learn from your mistakes... You will learn a lot today.");
    fortune.add("If you have something good in your life, don't let it go!");
    fortune.add("What ever you're goal is in life, embrace it visualize it, and for it will be yours.");
    fortune.add("Your shoes will make you happy today.");
    fortune.add("You cannot love life until you live the life you love.");
    fortune.add("Be on the lookout for coming events; They cast their shadows beforehand.");
    fortune.add("Land is always on the mind of a flying bird.");
    fortune.add("The man or woman you desire feels the same about you.");
    fortune.add("Meeting adversity well is the source of your strength.");
    fortune.add("A dream you have will come true.");
    fortune.add("Our deeds determine us, as much as we determine our deeds.");
    fortune.add("Never give up. You're not a failure if you don't give up.");
    fortune.add("You will become great if you believe in yourself.");
    fortune.add("There is no greater pleasure than seeing your loved ones prosper.");
    fortune.add("You will marry your lover.");
    fortune.add("The greatest love is self-love.");
    fortune.add("A very attractive person has a message for you.");
    fortune.add("You already know the answer to the questions lingering inside your head.");
    fortune.add("It is now, and in this world, that we must live.");
    fortune.add("You must try, or hate yourself for not trying.");
    fortune.add("You can make your own happiness.");
    fortune.add("The greatest risk is not taking one.");
    fortune.add("The love of your life is stepping into your planet this summer.");
    fortune.add("Love can last a lifetime, if you want it to.");
    fortune.add("Adversity is the parent of virtue.");
    fortune.add("Serious trouble will bypass you.");
    fortune.add("A short stranger will soon enter your life with blessings to share.");
    fortune.add("Now is the time to try something new.");
    fortune.add("Wealth awaits you very soon.");
    fortune.add("If you feel you are right, stand firmly by your convictions.");
    fortune.add("If winter comes, can spring be far behind?");
    fortune.add("Keep your eye out for someone special.");
    fortune.add("You are very talented in many ways.");
    fortune.add("A stranger, is a friend you have not spoken to yet.");
    fortune.add("A new voyage will fill your life with untold memories.");
    fortune.add("You will travel to many exotic places in your lifetime.");
    fortune.add("Your ability for accomplishment will follow with success.");
    fortune.add("Nothing astonishes men so much as common sense and plain dealing.");
    fortune.add("Its amazing how much good you can do if you dont care who gets the credit.");
    fortune.add("Everyone agrees. You are the best.");
    fortune.add("LIFE CONSISTS NOT IN HOLDING GOOD CARDS, BUT IN PLAYING THOSE YOU HOLD WELL.");
    fortune.add("Jealousy doesn't open doors, it closes them!");
    fortune.add("It's better to be alone sometimes.");
    fortune.add("When fear hurts you, conquer it and defeat it!");
    fortune.add("Let the deeds speak.");
    fortune.add("You will be called in to fulfill a position of high honor and responsibility.");
    fortune.add("The man on the top of the mountain did not fall there.");
    fortune.add("You will conquer obstacles to achieve success.");
    fortune.add("Joys are often the shadows, cast by sorrows.");
    fortune.add("Fortune favors the brave.");
  }
}
