package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Coordinate {
  private final int x;
  private final int y;
}
