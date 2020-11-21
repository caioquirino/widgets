package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class WidgetModel {
  private long id;

  private final Coordinate coordinate;
  private final int zindex;
  private final int width;
  private final int height;
}
