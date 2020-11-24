package com.miro.testtask.widgets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class WidgetFilter {
  private final @NonNull Integer x;
  private final @NonNull Integer y;
  private final @NonNull Integer width;
  private final @NonNull Integer height;
}
