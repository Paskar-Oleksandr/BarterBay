package com.barterbay.app.telegram;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static com.barterbay.app.telegram.ComplimentBot.START_COMMAND;
import static com.barterbay.app.telegram.ComplimentBot.createSendMessageForTelegram;
import static com.barterbay.app.util.CollectionsUtil.getRandomSetElement;

@Service
@Profile("prod")
public class PredictionBot extends TelegramLongPollingBot {

  private Set<String> predictions;

  @Value("${prediction-telegram-bot.name}")
  private String botUsername;

  @Value("${prediction-telegram-bot.token}")
  private String botToken;

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
    final SendMessage sendMessage;
    final var botCommand = update.getMessage().getText();

    if (START_COMMAND.equals(botCommand)) {
      final var userFirstName = update.getMessage().getFrom().getFirstName();
      final var messageText = "Dear "
        .concat(userFirstName)
        .concat(", цей бот написаний спеціально для Вас.")
        .concat(" Ксенія, для того щоб отримати передбачення, просто напиши - '/prediction'");
      sendMessage = createSendMessageForTelegram(update, messageText);
    } else {
      sendMessage = createPrediction(update);
    }
    execute(sendMessage);
  }

  private SendMessage createPrediction(Update update) {
    final var userFirstName = update.getMessage().getFrom().getFirstName();
    final var randomPrediction = getRandomSetElement(predictions);
    final var messageText = userFirstName
      .concat(", ")
      .concat(randomPrediction);
    return createSendMessageForTelegram(update, messageText);
  }

  @EventListener(ContextRefreshedEvent.class)
  public void fillPredictionsSet() {
    predictions = new HashSet<>();
    predictions.add("Чим менше в голові очікувань, тим більше в житті сюрпризів");
    predictions.add("Не витрачайте даремно життя на сумніви і страхи.");
    predictions.add("На Вас очікують любовні пригоди - наберіться лише терпіння.");
    predictions.add("Досить заглядати у майбутнє - живіть теперішнім днем.");
    predictions.add("Пора зробити паузу і насолодитися сьогоднішнім днем Ксюш :) ");
    predictions.add("Шукаєш щастя? А воно зовсім близько Ксюх, називається - Java world ^^");
    predictions.add("Все набридло, так? Наберіться терпіння - і ти побачите чудову веселку після доща. ");
    predictions.add("Пора на море! А коли Ви востаннє відпочивали? От-от!");
    predictions.add("Потіште себе чимось смачненьким! І пам'ятайте, що все, що не робиться, робиться лише на краще.");
    predictions.add("Не все, що ми втрачаємо, є втратою! Пам'ятайте про це!");
    predictions.add("Попри все, лише - вперед! До цілі! ");
    predictions.add("Усміхніться, адже хтось може закохатися у Вашу усмішку!");
    predictions.add("Настав час неймовірних пригод та незабутніх вражень!");
    predictions.add("Якщо шукаєте знаків від долі - ось він! Дійте! ");
    predictions.add("Все, що нас ламає, але не вбиває, робить сильнішим!");
    predictions.add("Все, що нас ламає, але не вбиває, робить сильнішим! ");
    predictions.add("Нові знання принесуть Вам успіх (я про джаву еслі що)");
    predictions.add("Багато працюй – отримаєш нагороду.\n");
    predictions.add("Вставай рано і опинишся вчасно на роботі або тренуванні");
    predictions.add("Не чиніть опір новому, життя зміниться в кращу сторону.");
    predictions.add("Постарайтеся не вступати ні з ким в конфлікт сьогодні. Сварка може призвести до жахливих наслідків");
    predictions.add("Просіть допомога залу – друзі вам допоможуть");
    predictions.add("Ваші цілі цілком досяжні. Терміново розробіть план дій");
    predictions.add("Заіржавілий замок, нарешті, відкриється, і зробите це ви!");
    predictions.add("Надії збудуться швидше, ніж ви очікуєте");
    predictions.add("Ваше діло праве, ви доб'єтеся перемоги, успіх за вами.");
    predictions.add("Терміново купуйте лотерею – вас очікує виграш.");
    predictions.add("Грамота - не за горами ( я про джаву звісно)");
    predictions.add("У вас закохані безнадійно, але поки хз хто це, треба трошки часу щоб визначити це");
    predictions.add("Не слухайте нічиїх порад (крім моїх по джаві), і удача буде на вашому боці.");
    predictions.add("Вас чекає цікава пригода.");
    predictions.add("Гроші зваляться вам прямо з небес. (якщо будеш гризти джаву)");
    predictions.add("Темрява розсіюється, попереду світло!");
    predictions.add("Дорогу здолає той, хто йде.");
    predictions.add("Вам пора вибратися з квартирі (лол, без сексизму, таке от передбачення)");
    predictions.add("Просто скажіть «Так!» цій джаві!");
    predictions.add("До осені ваші почуття стануть більш глибокими. Сьогодні рішення приймати рано.");
    predictions.add("Почекайте трошки, і наступний день принесе вам несподівану довгоочікувану радість.");
    predictions.add("Насолоджуючись успіхом і діліться нею з оточуючими вас людьми.");
    predictions.add("Будьте уважні до підказок долі.");
    predictions.add("В вашому житті скоро неодмінно з'явиться той чоловік, для якого ви будете головною цінністю в житті.");
    predictions.add("Щоб не відчувати себе дуже самотнім, допоможіть й іншим також досягти вершини.");
    predictions.add("Якщо істинно бажаєш бути щасливим - навчись панувати своїми думками.");
    predictions.add("На вас проллється дощ удачі.");
    predictions.add("Чим більше ви полюєте за великими грошима, тим далі вони віддаляються від вас. Припиніть часто думати про багатство, і воно неодмінно прийде до вас саме");
    predictions.add("Скоро ти будеш мати задоволення витрачати багато грошей, а також мати задоволення їх заробити");
    predictions.add("Ніхто не переможений до тих пір, поки не визнає себе переможеним.");
    predictions.add("Ви завжди отримаєте те, що потрібно. Через ваше чарівність та індивідуальність.");
    predictions.add("Ваше майбутнє так само безмежно, як високі небеса.");
    predictions.add("Через 5 місяців,5 тижнів і 5 днів відбудеться те, що кардинально змінить все ваше життя. (іф ю ноу вот ай мін)");
    predictions.add("Запобігай омани спокоєм, врівноваженістю і сомообладанием.");
    predictions.add("Ваш талант буде визнаний і належним чином винагороджений.");
    predictions.add("Краще жаліти про те що зробив, а не про те, що не зробив.");
    predictions.add("Той хто не чекає подяки, ніколи не буде розчарований.");
    predictions.add("Скоро ти позбудешся від поганої звички і матимеш дві нових.");
    predictions.add("Вам варто трохи почекати, і майбутнє принесе вам велику радість.");
    predictions.add("Зовсім скоро вас чекає романтична подорож.");
    predictions.add("Ніколи не сумуй: веселися, прагни, грай і гризи грамоту!");
    predictions.add("Пройди свої шляхи і до досягнень дойди");
    predictions.add("Людина здaтнa зmінити cвoє життя, змінюючи лишe cвoю тoчкy зopy");
    predictions.add("У найближчі дні твоє заповітне бажання здійсниться.");
    predictions.add("Твої батьки пишаються тобою!");
    predictions.add("Вивчи таблицю множення. І вчитель поставить тобі п'ятірку.");
    predictions.add("Зовсім скоро у тебе з'являться нові друзі.");
    predictions.add("Ведіть звичайне життя незвичайним способом.");
    predictions.add("Не шукайте зовнішніх ворогів: щоб зрозуміти, що заважає Вашому розвитку, загляньте всередину себе.");
    predictions.add("Пам'ятайте, що справжнє партнерство може існувати тільки між цільними особистостями.");
    predictions.add("Якщо колодязь засмічений, то саме час його очистити.");
    predictions.add("Виграш виходить від того, з чим Ви повинні розлучитися.");
    predictions.add("Дійте у відповідності не зі старими авторитетами, а з тим, що Ви вважаєте правильним для Вас.");
    predictions.add("Якщо не хочете серйозних потрясінь, проаналізуйте Ваше ставлення до власної особистості.");
    predictions.add("Відпустіть своє минуле: воно вичерпало себе.");
    predictions.add("Не чекайте занадто багато чого і не думайте про кінцевий результат.");
    predictions.add("Вивчайте Ваші тіньові сторони; зрозумійте, що притягує в Ваше життя нещастя.");
    predictions.add("Завершіть те, що почали.");
    predictions.add("Будьте терплячі, і якщо рішення Ваше правильно, Всесвіт підтримає його.");
    predictions.add("Не піддавайтеся емоціям.");
    predictions.add("Придивіться до свого здоров'я.");
    predictions.add("Насолоджуйтесь удачею і діліться нею з оточуючими Вас людьми.");
    predictions.add("Зосередьтеся на сьогоденні.");
    predictions.add("Будьте наполегливі в битві з власним егоїзмом.");
    predictions.add("Прийшов час діяти, навіть якщо від Вас потрібно стрибнути в порожнечу.");
    predictions.add("Розкрийтеся і пропустіть світло в ту частину свого життя, яка до сих пір була таємницею.");
    predictions.add("Ну, розумієте можна, звичайно, і зайця навчити курити ... Немає нічого неможливого для людини з інтелектом.");
    predictions.add("Жити добре! А добре жити - ще краще!");
    predictions.add("Добре зроблене краще, ніж добре сказане.");
    predictions.add("Це час, щоб рухатися. Ваш настрій покращиться.");
    predictions.add("Головне - не забути головне. А то забудеш головне, а це головне!");
    predictions.add("Немає безвихідних ситуацій. звичайно вихід є там же де і вхід.");
    predictions.add("Вас чекає зустріч з важливою людиною.");
    predictions.add("Прийшов час закінчити старе і почати нове.");
    predictions.add("Відповідь на Ваше питання пов'язаний з якимось чоловіком, можливо, добре Вам відомим.");
    predictions.add("У Ваше життя увійде щось нове, що значно вплине на Вашу особу.");
    predictions.add("Ви сподіваєтеся недаремно!");
    predictions.add("Ви сидите на морському березі, але чомусь не можете вдихнути глибоко, на повні груди.");
    predictions.add("Наберіться сміливості: і тоді удача прийде до вам з чарівних країн - країн світу Джави");
    predictions.add("Твоя половинка вважає, що ти супер");
    predictions.add("Вас чекає щось хвилююче");
    predictions.add("Ваша половинка покаже Вам свої почуття");
    predictions.add("Ви будете безмежно щасливі");
    predictions.add("Ви будете жити не озираючись");
    predictions.add("Вас чекає щось особливе");
    predictions.add("Ви відчуєте аромат любові");
    predictions.add("Вас чекає щось миле і озорное ...");
    predictions.add("Емоції захлиснуть Вас");
    predictions.add("Вас чекає щось захоплююче для вас обох");
    predictions.add("Ви зрозумієте, хто любов всього Вашого життя");
    predictions.add("Ви будете в серці один одного");
    predictions.add("Вас чекає відповідальна розмова");
    predictions.add("Ваша половинка буде вкладати в Вас все");
    predictions.add("Ви отримаєте відповіді на свої питання");
    predictions.add("Вас буде зігрівати тепло коханої людини");
    predictions.add("Ви знаходитесь на початку чогось нового (ну, ти ж розумієш про що я)");
    predictions.add("Разом ви відкриєте нову сторінку");
    predictions.add("Ви отримаєте добрий знак");
    predictions.add("Світ коханої людини буде обертатися навколо");
    predictions.add("Ви легко переживете ускладнення в стосунках");
  }
}
