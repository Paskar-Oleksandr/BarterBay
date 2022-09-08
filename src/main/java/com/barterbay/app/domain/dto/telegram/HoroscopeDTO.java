package com.barterbay.app.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoroscopeDTO {

  @JsonProperty("date_range")
  private String dateRange;

  @JsonProperty("current_date")
  private String currentDate;

  private String description;

  private String compatibility;

  private String mood;

  private String color;

  @JsonProperty("lucky_number")
  private String luckyNumber;

  @JsonProperty("lucky_time")
  private String luckyTime;

  @Override
  public String toString() {
    return "Stasya, here is your horoscope:\n" +
      "Birthdays date range - " + dateRange + "\n" +
      "Selected date - " + currentDate + "\n" +
      "Horoscope prediction - " + description + "\n" +
      "The best sign that fits - " + compatibility + "\n" +
      "Mood - " + mood + "\n" +
      "Best color - " + color + "\n" +
      "LuckyNumber - " + luckyNumber + "\n" +
      "LuckyTime - " + luckyTime + "\n";
  }
}
