package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class WidgetModel {

  private final Integer zindex;
  @NonNull
  private final Coordinate coordinate;
  @NonNull
  private final int width;
  @NonNull
  private final int height;
  private Long id;
}
