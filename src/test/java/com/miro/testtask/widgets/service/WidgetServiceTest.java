package com.miro.testtask.widgets.service;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WidgetServiceTest {

  private final WidgetRepository widgetRepository = mock(WidgetRepository.class);

  private final WidgetService widgetService = new WidgetService(widgetRepository);

  @AfterEach
  void cleanup() {
    reset(widgetRepository);
  }

  @Test
  void findAll() {
    var widgetList = List.of(
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(1).y(2).build())
            .height(10)
            .width(100)
            .zindex(1)
            .id(1)
            .build(),
        WidgetModel.builder()
            .coordinate(Coordinate.builder().x(3).y(4).build())
            .height(10)
            .width(100)
            .zindex(2)
            .id(2)
            .build()
    );

    when(widgetRepository.findAll()).thenReturn(widgetList);

    var result = widgetService.findAll();

    assertEquals(widgetList, result);
  }

  @Test
  void create() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .build();

    when(widgetRepository.create(widget)).thenReturn(widget.toBuilder().id(1).build());

    var createdWidget = widgetService.create(widget);

    assertEquals(1, createdWidget.getId());
  }

  @Test
  void update() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .id(1)
        .build();

    when(widgetRepository.update(widget)).thenReturn(widget.toBuilder().id(1).build());

    var createdWidget = widgetService.update(widget);

    assertEquals(widget, createdWidget);
  }

  @Test
  void delete() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .id(1)
        .build();

    widgetService.delete(widget);

    verify(widgetRepository, times(1)).delete(eq(widget));
  }
}
