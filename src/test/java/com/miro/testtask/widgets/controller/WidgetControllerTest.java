package com.miro.testtask.widgets.controller;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.service.WidgetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class WidgetControllerTest {

  private final WidgetService widgetService = mock(WidgetService.class);

  private final WidgetController widgetController = new WidgetController(widgetService);

  @AfterEach
  void cleanup() {
    reset(widgetService);
  }

  @Test
  void getAll() {
    var widgetList = List.of(
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(1).y(2).build())
            .height(10)
            .width(100)
            .zIndex(1)
            .id(1)
            .build(),
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(3).y(4).build())
            .height(10)
            .width(100)
            .zIndex(2)
            .id(2)
            .build()
    );

    when(widgetService.getAll()).thenReturn(widgetList);

    var result = widgetController.getAll();

    assertEquals(widgetList, result);
  }

  @Test
  void create() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zIndex(1)
        .build();

    when(widgetService.create(widget)).thenReturn(widget.toBuilder().id(1).build());

    var createdWidget = widgetController.create(widget);

    assertEquals(1, createdWidget.getId());
  }

  @Test
  void update() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zIndex(1)
        .id(1)
        .build();

    when(widgetService.update(widget)).thenReturn(widget.toBuilder().id(1).build());

    var createdWidget = widgetController.update(widget);

    assertEquals(widget, createdWidget);
  }

  @Test
  void delete() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zIndex(1)
        .id(1)
        .build();

    widgetController.delete(widget);

    verify(widgetService, times(1)).delete(eq(widget));
  }

}
