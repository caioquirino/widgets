package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@Builder
public class Coordinate {

  @NonNull
  private final int x;

  @NonNull
  private final int y;
}
