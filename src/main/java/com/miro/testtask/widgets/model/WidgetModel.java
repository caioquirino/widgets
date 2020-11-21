package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WidgetModel {
  private int id;

  private Coordinate coordinate;
  private int zIndex;
  private int width;
  private int height;
}
