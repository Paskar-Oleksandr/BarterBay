package com.barterbay.app.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class CollectionsUtil {

  public static <E> E getRandomSetElement(Collection<E> collection) {
    return collection.stream()
      .skip(ThreadLocalRandom.current().nextInt(collection.size()))
      .findFirst()
      .orElseThrow();
  }
}
