package com.miro.testtask.widgets.service;

import com.miro.testtask.widgets.model.Coordinate;
import com.miro.testtask.widgets.model.WidgetFilter;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WidgetServiceTest {

  private final WidgetRepository widgetRepository = mock(WidgetRepository.class);

  private final WidgetService widgetService = new WidgetService(widgetRepository);

  private final List<WidgetModel> fixtures = List.of(
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

  @AfterEach
  void cleanup() {
    verifyNoMoreInteractions(widgetRepository);
    reset(widgetRepository);
  }

  @Test
  void findAll() {
    when(widgetRepository.findAll()).thenReturn(fixtures);

    var result = widgetService.findAll();

    verify(widgetRepository, times(1)).findAll();
    assertEquals(fixtures, result);
  }

  @Test
  void findPaged() {
    var pageable = PageRequest.of(0, 1);

    var returnedPage = new PageImpl<>(
        fixtures.stream().limit(1).collect(Collectors.toUnmodifiableList()),
        pageable,
        fixtures.size()
    );

    when(widgetRepository.findAll(eq(pageable))).thenReturn(returnedPage);

    var result = widgetService.findPaged(pageable);

    verify(widgetRepository, times(1)).findAll(eq(pageable));
    assertEquals(returnedPage, result);
  }

  @Test
  void findFilteredPaged() {
    var pageable = PageRequest.of(0, 1);
    var filter = WidgetFilter
        .builder()
        .y(0).x(0)
        .height(1000).width(1000)
        .build();

    var returnedPage = new PageImpl<>(
        fixtures.stream().limit(1).collect(Collectors.toUnmodifiableList()),
        pageable,
        fixtures.size()
    );

    when(widgetRepository.FindAll(eq(pageable), eq(filter))).thenReturn(returnedPage);

    var result = widgetService.findFilteredPaged(pageable, filter);

    verify(widgetRepository, times(1)).FindAll(eq(pageable), eq(filter));
    assertEquals(returnedPage, result);
  }

  @Test
  void create() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(3)
        .build();

    when(widgetRepository.create(eq(widget))).thenReturn(widget.toBuilder().id(1L).build());
    when(widgetRepository.findByZindex(eq(3))).thenReturn(Optional.empty());

    var createdWidget = widgetService.create(widget);
    verify(widgetRepository, times(1)).findByZindex(eq(3));
    verify(widgetRepository, times(1)).create(widget);

    assertEquals(1, createdWidget.getId());
  }

  @Test
  void createWithoutZindex() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .build();

    when(widgetRepository.create(eq(widget.toBuilder().zindex(6).build())))
        .thenReturn(widget.toBuilder().zindex(6).id(1L).build());
    when(widgetRepository.maximumZindex()).thenReturn(5);

    var createdWidget = widgetService.create(widget);

    verify(widgetRepository, times(1)).maximumZindex();
    verify(widgetRepository, times(1)).create(eq(widget.toBuilder().zindex(6).build()));
    assertEquals(1, createdWidget.getId());

  }

  @Test
  void createWithDuplicatedZindex() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(1)
        .id(1L)
        .build();

    when(widgetRepository.create(eq(widget))).thenReturn(widget.toBuilder().id(1L).zindex(3).build());
    when(widgetRepository.findByZindex(1)).thenReturn(Optional.of(this.fixtures.get(0)));
    when(widgetRepository.findByZindex(2)).thenReturn(Optional.of(this.fixtures.get(1)));
    when(widgetRepository.findByZindex(3)).thenReturn(Optional.empty());

    var created = widgetService.create(widget);

    verify(widgetRepository, times(1)).create(eq(widget.toBuilder().zindex(1).build()));
    verify(widgetRepository, times(1)).update(eq(this.fixtures.get(0).toBuilder().zindex(2).build()));
    verify(widgetRepository, times(1)).update(eq(this.fixtures.get(1).toBuilder().zindex(3).build()));
    verify(widgetRepository, times(3)).findByZindex(anyInt());

    assertEquals(widget.toBuilder().zindex(3).build(), created);
  }

  @Test
  void update() {
    var widget = WidgetModel.builder()
        .coordinate(Coordinate.builder().x(1).y(2).build())
        .height(10)
        .width(100)
        .zindex(10)
        .id(1L)
        .build();

    when(widgetRepository.update(eq(widget))).thenReturn(widget.toBuilder().id(1L).build());
    when(widgetRepository.findByZindex(10)).thenReturn(Optional.empty());

    var updated = widgetService.update(widget);

    verify(widgetRepository, times(1)).update(eq(widget));
    verify(widgetRepository, times(1)).findByZindex(10);

    assertEquals(widget, updated);
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

    widgetService.delete(widget);

    verify(widgetRepository, times(1)).delete(eq(widget));
  }
}
