package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class WidgetModel {
  private long id;

  private Coordinate coordinate;
  private int zIndex;
  private int width;
  private int height;
}
