package com.miro.testtask.widgets.service;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WidgetServiceTest {

  @Test
  void getAll() {
    var widgetRepository = mock(WidgetRepository.class);

    var widgetService = new WidgetService(widgetRepository);

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

    when(widgetRepository.getAll()).thenReturn(widgetList);

    var result = widgetService.getAll();

    assertEquals(widgetList, result);
  }

  @Test
  void create() {
    var widgetRepository = mock(WidgetRepository.class);

    var widgetService = new WidgetService(widgetRepository);

    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zIndex(1)
        .build();

    when(widgetRepository.create(widget)).thenReturn(widget.toBuilder().id(1).build());

    var createdWidget = widgetService.create(widget);

    assertEquals(1, createdWidget.getId());
  }

  @Test
  void update() {
    var widgetRepository = mock(WidgetRepository.class);

    var widgetService = new WidgetService(widgetRepository);

    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zIndex(1)
        .id(1)
        .build();

    when(widgetRepository.update(widget)).thenReturn(widget.toBuilder().id(1).build());

    var createdWidget = widgetService.update(widget);

    assertEquals(widget, createdWidget);
  }

  @Test
  void delete() {
    var widgetRepository = mock(WidgetRepository.class);

    var widgetService = new WidgetService(widgetRepository);

    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zIndex(1)
        .id(1)
        .build();

    widgetService.delete(widget);

    verify(widgetRepository, times(1)).delete(eq(widget));
  }
}
