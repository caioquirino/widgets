package com.miro.testtask.widgets.controller;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.service.WidgetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WidgetControllerTest {

  private final WidgetService widgetService = mock(WidgetService.class);

  private final WidgetController widgetController = new WidgetController(widgetService, 10);

  @AfterEach
  void cleanup() {
    reset(widgetService);
  }

  private final List<WidgetModel> widgetList = List.of(
      WidgetModel.builder()
          .coordinate(Coordinate.builder().x(1).y(2).build())
          .height(10)
          .width(100)
          .zindex(1)
          .id(1L)
          .build(),
      WidgetModel.builder()
          .coordinate(Coordinate.builder().x(3).y(4).build())
          .height(10)
          .width(100)
          .zindex(2)
          .id(2L)
          .build()
  );

  @Test
  void findAll() {
    var pageable = PageRequest.of(0, 1);

    var returnedPage = new PageImpl<>(
        widgetList.stream().limit(1).collect(Collectors.toUnmodifiableList()),
        pageable,
        widgetList.size()
    );

    when(widgetService.findFilteredPaged(eq(pageable), isNull())).thenReturn(returnedPage);

    var result = widgetController.findAll(
        0,
        1,
        null,
        null,
        null,
        null
    );

    verify(widgetService, times(1)).findFilteredPaged(eq(pageable), isNull());
    assertEquals(returnedPage.getContent(), result);
  }

  @Test
  void create() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .build();

    when(widgetService.create(widget)).thenReturn(widget.toBuilder().id(1L).build());

    var createdWidget = widgetController.create(widget);

    assertEquals(1, createdWidget.getId());
  }

  @Test
  void update() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .id(1L)
        .build();

    when(widgetService.update(widget)).thenReturn(widget.toBuilder().id(1L).build());

    var createdWidget = widgetController.update(widget);

    assertEquals(widget, createdWidget);
  }

  @Test
  void delete() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .id(1L)
        .build();

    widgetController.delete(widget);

    verify(widgetService, times(1)).delete(eq(widget));
  }

}
