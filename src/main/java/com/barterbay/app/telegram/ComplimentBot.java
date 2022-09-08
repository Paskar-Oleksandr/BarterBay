package com.barterbay.app.telegram;

import com.barterbay.app.domain.dto.telegram.ComplimentDTO;
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.barterbay.app.util.CollectionsUtil.getRandomSetElement;
import static com.barterbay.app.util.Constants.START_COMMAND;

@Service
@Profile("prod")
public class ComplimentBot extends TelegramLongPollingBot {

  private Set<String> compliments;
  private final WebClient webClient;

  public ComplimentBot(WebClient webClient) {
    this.webClient = webClient;
  }

  @Value("${compliment-telegram-bot.name}")
  private String botUsername;

  @Value("${compliment-telegram-bot.token}")
  private String botToken;

  @Value("${random-compliment-api.url}")
  private String randomComplimentAPIUrl;

  @Override
  @SneakyThrows
  public void onUpdateReceived(Update update) {
    final SendMessage sendMessage;
    final var botCommand = update.getMessage().getText();

    if (START_COMMAND.equals(botCommand)) {
      final var userFirstName = update.getMessage().getFrom().getFirstName();
      final var messageText = "Dear "
        .concat(userFirstName)
        .concat(", I'm so happy to see you here.")
        .concat(" In order to get beautiful compliment from me - just write '/compliment'");
      sendMessage = createSendMessageForTelegram(update, messageText);
    } else {
      sendMessage = createCompliment(update);
    }
    execute(sendMessage);
  }

  private SendMessage createCompliment(Update update) {
    final var userFirstName = update.getMessage().getFrom().getFirstName();
    final String randomCompliment;
    if (ThreadLocalRandom.current().nextBoolean()) {
      randomCompliment = getRandomSetElement(compliments);
    } else {
      randomCompliment = getComplimentFromFreeAPI();
    }

    final var messageText = userFirstName
      .concat(", ")
      .concat(randomCompliment);
    return createSendMessageForTelegram(update, messageText);
  }

  private String getComplimentFromFreeAPI() {
    return webClient
      .get()
      .uri(randomComplimentAPIUrl)
      .retrieve()
      .bodyToMono(ComplimentDTO.class)
      .map(ComplimentDTO::getCompliment)
      .block();
  }

  public static SendMessage createSendMessageForTelegram(Update update, String messageText) {
    final var sendMessage = new SendMessage();
    sendMessage.setText(messageText);
    sendMessage.setChatId(update.getMessage().getChatId());
    return sendMessage;
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @EventListener(ContextRefreshedEvent.class)
  public void fillComplimentsSet() {
    compliments = new HashSet<>();
    compliments.add("I am really impressed with how well you listen to everything that everyone says. It is obvious that you listen to everyone before you make a decision on what to do next.");
    compliments.add("You really have the ability to light up a room as soon as you walk in. It is obvious that everyone loves spending time around you, including me.");
    compliments.add("I am really impressed with how passionate you are about staying in shape. You are a role model for everyone who wants to take care of their physical health.");
    compliments.add("You are a very strong, resilient person who is able to handle just about everything. You are a great role model for everyone who looks up to you. ");
    compliments.add("You have a truly infectious personality that makes a positive impact on everyone around you. You instantly bring happiness to everyone who spends time around you. ");
    compliments.add("I'm really impressed with your taste and style. It is really obvious that you have thought carefully about how to decorate the inside of this room.");
    compliments.add("I always love spending time around you because you have such creative ideas. It is never boring to hang out with you! ");
    compliments.add("I am really impressed with how creative you are. I never knew just how creative you could be, but taking a look at what you've done recently, it is obvious that you have a knack for it. ");
    compliments.add("I really admire how you handle difficult situations. It is not easy to go through everything that you have been through, and you always seem to handle it was such aplomb.");
    compliments.add("You have an innate ability to inspire everyone around you. It seems like everyone around you instantly does better as soon as I talk to you. I hope I have the same impact on people one day. ");
    compliments.add("I'm really impressed with how well you play your musical instrument. I never realized just how good you were at music. I could listen to you play for hours on end. ");
    compliments.add("You have a sense of confidence that is very refreshing. I never have to worry about your self-esteem, as you believe in yourself just as much as anyone I have ever seen.");
    compliments.add("I always love how you are willing to help someone out. No matter what problems someone might have, you are always willing to put their needs ahead of you around, which sets a great example.");
    compliments.add("You are a great role model for everyone else around you. There are lots of people who look up to you, and it is obvious that you take that very seriously. ");
    compliments.add("I am really impressed with how well you keep yourself in shape. Even though you are very busy, you always seem to make time to exercise, which is going to pay dividends down the road. ");
    compliments.add("I really respect just how much you stand up for what you believe in. It seems like no matter what happens, you are never willing to sacrifice your beliefs or something else. ");
    compliments.add("You are a natural-born leader. It is obvious that people follow you wherever you go. Your ability to inspire others to follow you is infectious. ");
    compliments.add("Your mind works in such a unique way. You always see things from a different perspective, which is a great way to teach others. Never lose that about yourself. ");
    compliments.add("You always seem to know just what to say! No matter how bad I am feeling, you always know just what to say to raise my spirits. That quality is irreplaceable. ");
    compliments.add("I never realized just how great your sense of humor was! You always know just what to say to make someone else laugh. That is a great way to maintain relationships throughout your life. ");
    compliments.add("I really admire just how well you are able to stay in touch with the people in your life. No matter how busy you get, you always make time for those important to you. ");
    compliments.add("I consider myself lucky to have gotten to know you. You have had a major impact on my life, and I truly appreciate it.");
    compliments.add("You always let the other person know just how much they matter to you. No matter how busy you are, you always let them know that your relationship with that matters to you.");
    compliments.add("I'm a better person for getting to know you. I cannot measure the impact you've had in my life, but I know it is something I will carry with me forever. ");
    compliments.add("I always feel like I am happy when I am around you. I really appreciate how you make time for me.");
    compliments.add("I love how I can always depend on you. If you say you are going to do something, you always find a way to get it done. ");
    compliments.add("You are one of the most reliable people I know. I am always impressed with how you come through every time. ");
    compliments.add("The world is a better place with you in it. Even though there are difficult times, you always seem to find the bright spot.");
    compliments.add("I love how you never seem to make the same mistake twice. If you do something wrong, you learn from that, and instantly become a better person. Everyone else can learn from you. ");
  }
}
